/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package cn.sliew.flinkful.sql.catalog.sakura.catalog.service.impl;

import cn.sliew.flinkful.sql.catalog.common.dict.catalog.CatalogType;
import cn.sliew.flinkful.sql.catalog.common.dict.catalog.flink.CatalogTableKind;
import cn.sliew.flinkful.sql.catalog.repository.dao.entity.CatalogDatabase;
import cn.sliew.flinkful.sql.catalog.repository.dao.entity.CatalogFunction;
import cn.sliew.flinkful.sql.catalog.repository.dao.entity.CatalogTable;
import cn.sliew.flinkful.sql.catalog.repository.dao.mapper.CatalogDatabaseMapper;
import cn.sliew.flinkful.sql.catalog.repository.dao.mapper.CatalogFunctionMapper;
import cn.sliew.flinkful.sql.catalog.repository.dao.mapper.CatalogTableMapper;
import cn.sliew.flinkful.sql.catalog.sakura.catalog.service.CatalogService;
import cn.sliew.flinkful.sql.catalog.sakura.catalog.service.convert.CatalogDatabaseConvert;
import cn.sliew.flinkful.sql.catalog.sakura.catalog.service.convert.CatalogFunctionConvert;
import cn.sliew.flinkful.sql.catalog.sakura.catalog.service.convert.CatalogTableConvert;
import cn.sliew.flinkful.sql.catalog.sakura.catalog.service.dto.CatalogDatabaseDTO;
import cn.sliew.flinkful.sql.catalog.sakura.catalog.service.dto.CatalogFunctionDTO;
import cn.sliew.flinkful.sql.catalog.sakura.catalog.service.dto.CatalogTableDTO;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.apache.flink.table.catalog.ObjectPath;
import org.apache.flink.table.catalog.exceptions.*;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public class CatalogServiceImpl implements CatalogService {

    private final SqlSessionFactory sqlSessionFactory;

    public CatalogServiceImpl(SqlSessionFactory sqlSessionFactory) {
        this.sqlSessionFactory = sqlSessionFactory;
    }

    @Override
    public List<CatalogDatabaseDTO> listDatabases(CatalogType type, String catalog) {
        try (SqlSession sqlSession = sqlSessionFactory.openSession()) {
            CatalogDatabaseMapper catalogDatabaseMapper = sqlSession.getMapper(CatalogDatabaseMapper.class);
            LambdaQueryWrapper<CatalogDatabase> queryWrapper = Wrappers.lambdaQuery(CatalogDatabase.class)
                    .eq(CatalogDatabase::getType, type)
                    .eq(CatalogDatabase::getCatalog, catalog)
                    .orderByAsc(CatalogDatabase::getName);
            List<CatalogDatabase> databases = catalogDatabaseMapper.selectList(queryWrapper);
            return CatalogDatabaseConvert.INSTANCE.toDto(databases);
        }
    }

    @Override
    public Optional<CatalogDatabaseDTO> getDatabase(CatalogType type, String catalog, String database) {
        try (SqlSession sqlSession = sqlSessionFactory.openSession()) {
            CatalogDatabaseMapper catalogDatabaseMapper = sqlSession.getMapper(CatalogDatabaseMapper.class);
            LambdaQueryWrapper<CatalogDatabase> queryWrapper = Wrappers.lambdaQuery(CatalogDatabase.class)
                    .eq(CatalogDatabase::getCatalog, catalog)
                    .eq(CatalogDatabase::getName, database);
            CatalogDatabase record = catalogDatabaseMapper.selectOne(queryWrapper);
            if (record == null) {
                return Optional.empty();
            }
            return Optional.of(CatalogDatabaseConvert.INSTANCE.toDto(record));
        }
    }

    @Override
    public boolean databaseExists(CatalogType type, String catalog, String database) {
        Optional<CatalogDatabaseDTO> optional = getDatabase(type, catalog, database);
        return optional.isPresent();
    }

    @Override
    public void insertDatabase(CatalogDatabaseDTO database) throws DatabaseAlreadyExistException {
        try (SqlSession sqlSession = sqlSessionFactory.openSession()) {
            CatalogDatabaseMapper catalogDatabaseMapper = sqlSession.getMapper(CatalogDatabaseMapper.class);
            if (databaseExists(database.getType(), database.getCatalog(), database.getName())) {
                throw new DatabaseAlreadyExistException(database.getCatalog(), database.getName());
            }
            CatalogDatabase record = CatalogDatabaseConvert.INSTANCE.toDo(database);
            catalogDatabaseMapper.insert(record);
            sqlSession.commit();
        }
    }

    @Override
    public void updateDatabase(CatalogDatabaseDTO database) throws DatabaseNotExistException {
        try (SqlSession sqlSession = sqlSessionFactory.openSession()) {
            CatalogDatabaseMapper catalogDatabaseMapper = sqlSession.getMapper(CatalogDatabaseMapper.class);
            if (databaseExists(database.getType(), database.getCatalog(), database.getName()) == false) {
                throw new DatabaseNotExistException(database.getCatalog(), database.getName());
            }
            CatalogDatabase record = CatalogDatabaseConvert.INSTANCE.toDo(database);
            catalogDatabaseMapper.updateById(record);
            sqlSession.commit();
        }
    }

    @Override
    public void deleteDatabase(CatalogType type, String catalog, String database) throws DatabaseNotExistException {
        try (SqlSession sqlSession = sqlSessionFactory.openSession()) {
            CatalogDatabaseMapper catalogDatabaseMapper = sqlSession.getMapper(CatalogDatabaseMapper.class);
            if (databaseExists(type, catalog, database) == false) {
                throw new DatabaseNotExistException(catalog, database);
            }
            LambdaQueryWrapper<CatalogDatabase> queryWrapper = Wrappers.lambdaQuery(CatalogDatabase.class)
                    .eq(CatalogDatabase::getType, type)
                    .eq(CatalogDatabase::getCatalog, catalog)
                    .eq(CatalogDatabase::getName, database);

            CatalogDatabase record = new CatalogDatabase();
            record.setType(type);
            record.setCatalog(catalog);
            record.setName(database);
            catalogDatabaseMapper.update(record, queryWrapper);
            sqlSession.commit();
        }
    }

    @Override
    public boolean isDatabaseEmpty(CatalogType type, String catalog, String database) {
        try (SqlSession sqlSession = sqlSessionFactory.openSession()) {
            CatalogTableMapper catalogTableMapper = sqlSession.getMapper(CatalogTableMapper.class);
            CatalogFunctionMapper catalogFunctionMapper = sqlSession.getMapper(CatalogFunctionMapper.class);
            int tableCount = catalogTableMapper.countByDatabase(type, catalog, database, CatalogTableKind.TABLE);
            int functionCount = catalogFunctionMapper.countByDatabase(type, catalog, database);
            return tableCount != 0 || functionCount != 0;
        }
    }

    @Override
    public List<CatalogTableDTO> listTables(CatalogType type, String catalog, String database) {
        try (SqlSession sqlSession = sqlSessionFactory.openSession()) {
            CatalogTableMapper catalogTableMapper = sqlSession.getMapper(CatalogTableMapper.class);
            List<CatalogTable> records = catalogTableMapper.selectByDatabase(type, catalog, database, CatalogTableKind.TABLE);
            return CatalogTableConvert.INSTANCE.toDto(records);
        }
    }

    @Override
    public Optional<CatalogTableDTO> getTable(CatalogType type, String catalog, String database, String table) {
        try (SqlSession sqlSession = sqlSessionFactory.openSession()) {
            CatalogTableMapper catalogTableMapper = sqlSession.getMapper(CatalogTableMapper.class);
            Optional<CatalogTable> optional = catalogTableMapper.selectByName(type, catalog, database, CatalogTableKind.TABLE, table);
            return optional.map(record -> CatalogTableConvert.INSTANCE.toDto(record));
        }
    }

    @Override
    public boolean tableExists(CatalogType type, String catalog, String database, String table) {
        Optional<CatalogTableDTO> optional = getTable(type, catalog, database, table);
        return optional.isPresent();
    }

    @Override
    public void insertTable(CatalogType type, String catalog, String database, CatalogTableDTO table) throws DatabaseNotExistException, TableAlreadyExistException {
        try (SqlSession sqlSession = sqlSessionFactory.openSession()) {
            CatalogTableMapper catalogTableMapper = sqlSession.getMapper(CatalogTableMapper.class);
            CatalogDatabaseDTO catalogDatabaseDTO = getDatabase(type, catalog, database).orElseThrow(() -> new DatabaseNotExistException(catalog, database));
            if (tableExists(type, catalog, database, table.getName())) {
                throw new TableAlreadyExistException(catalog, new ObjectPath(database, table.getName()));
            }
            CatalogTable record = CatalogTableConvert.INSTANCE.toDo(table);
            record.setDatabaseId(catalogDatabaseDTO.getId());
            catalogTableMapper.insert(record);
            sqlSession.commit();
        }
    }

    @Override
    public void updateTable(CatalogType type, String catalog, String database, CatalogTableDTO table) throws TableNotExistException {
        try (SqlSession sqlSession = sqlSessionFactory.openSession()) {
            CatalogTableMapper catalogTableMapper = sqlSession.getMapper(CatalogTableMapper.class);
            if (tableExists(type, catalog, database, table.getName()) == false) {
                throw new TableNotExistException(catalog, new ObjectPath(database, table.getName()));
            }
            CatalogTable record = CatalogTableConvert.INSTANCE.toDo(table);
            catalogTableMapper.updateById(record);
            sqlSession.commit();
        }
    }

    @Override
    public void renameTable(CatalogType type, String catalog, String database, String currentName, String newName) throws TableAlreadyExistException, TableNotExistException {
        try (SqlSession sqlSession = sqlSessionFactory.openSession()) {
            CatalogTableMapper catalogTableMapper = sqlSession.getMapper(CatalogTableMapper.class);
            CatalogTableDTO catalogTableDTO = getTable(type, catalog, database, currentName).orElseThrow(() -> new TableNotExistException(catalog, new ObjectPath(database, currentName)));
            if (tableExists(type, catalog, database, newName)) {
                throw new TableAlreadyExistException(catalog, new ObjectPath(database, newName));
            }
            CatalogTable record = CatalogTableConvert.INSTANCE.toDo(catalogTableDTO);
            record.setName(newName);
            catalogTableMapper.updateById(record);
            sqlSession.commit();
        }
    }

    @Override
    public void deleteTable(CatalogType type, String catalog, String database, String table) throws TableNotExistException {
        try (SqlSession sqlSession = sqlSessionFactory.openSession()) {
            CatalogTableMapper catalogTableMapper = sqlSession.getMapper(CatalogTableMapper.class);
            if (!tableExists(type, catalog, database, table)) {
                throw new TableNotExistException(catalog, new ObjectPath(database, table));
            }
            catalogTableMapper.deleteByName(type, catalog, database, CatalogTableKind.TABLE, table);
            sqlSession.commit();
        }
    }

    @Override
    public List<CatalogTableDTO> listViews(CatalogType type, String catalog, String database) {
        try (SqlSession sqlSession = sqlSessionFactory.openSession()) {
            CatalogTableMapper catalogTableMapper = sqlSession.getMapper(CatalogTableMapper.class);
            List<CatalogTable> views = catalogTableMapper.selectByDatabase(type, catalog, database, CatalogTableKind.VIEW);
            return CatalogTableConvert.INSTANCE.toDto(views);
        }
    }

    @Override
    public Optional<CatalogTableDTO> getView(CatalogType type, String catalog, String database, String view) {
        try (SqlSession sqlSession = sqlSessionFactory.openSession()) {
            CatalogTableMapper catalogTableMapper = sqlSession.getMapper(CatalogTableMapper.class);
            Optional<CatalogTable> optional = catalogTableMapper.selectByName(type, catalog, database, CatalogTableKind.VIEW, view);
            return optional.map(record -> CatalogTableConvert.INSTANCE.toDto(record));
        }
    }

    @Override
    public boolean viewExists(CatalogType type, String catalog, String database, String view) {
        Optional<CatalogTableDTO> optional = getView(type, catalog, database, view);
        return optional.isPresent();
    }

    @Override
    public void insertView(CatalogType type, String catalog, String database, CatalogTableDTO view) throws DatabaseNotExistException, TableAlreadyExistException {
        try (SqlSession sqlSession = sqlSessionFactory.openSession()) {
            CatalogTableMapper catalogTableMapper = sqlSession.getMapper(CatalogTableMapper.class);
            CatalogDatabaseDTO catalogDatabaseDTO = getDatabase(type, catalog, database).orElseThrow(() -> new DatabaseNotExistException(catalog, database));
            if (viewExists(type, catalog, database, view.getName())) {
                throw new TableAlreadyExistException(catalog, new ObjectPath(database, view.getName()));
            }
            CatalogTable record = CatalogTableConvert.INSTANCE.toDo(view);
            record.setDatabaseId(catalogDatabaseDTO.getId());
            catalogTableMapper.insert(record);
            sqlSession.commit();
        }
    }

    @Override
    public void updateView(CatalogType type, String catalog, String database, CatalogTableDTO view) throws TableNotExistException {
        try (SqlSession sqlSession = sqlSessionFactory.openSession()) {
            CatalogTableMapper catalogTableMapper = sqlSession.getMapper(CatalogTableMapper.class);
            if (viewExists(type, catalog, database, view.getName()) == false) {
                throw new TableNotExistException(catalog, new ObjectPath(database, view.getName()));
            }
            CatalogTable record = CatalogTableConvert.INSTANCE.toDo(view);
            catalogTableMapper.updateById(record);
            sqlSession.commit();
        }
    }

    @Override
    public void renameView(CatalogType type, String catalog, String database, String currentName, String newName) throws TableNotExistException, TableAlreadyExistException {
        try (SqlSession sqlSession = sqlSessionFactory.openSession()) {
            CatalogTableMapper catalogTableMapper = sqlSession.getMapper(CatalogTableMapper.class);
            CatalogTableDTO catalogViewDTO = getView(type, catalog, database, currentName).orElseThrow(() -> new TableNotExistException(catalog, new ObjectPath(database, currentName)));
            if (viewExists(type, catalog, database, newName)) {
                throw new TableAlreadyExistException(catalog, new ObjectPath(database, newName));
            }
            CatalogTable record = CatalogTableConvert.INSTANCE.toDo(catalogViewDTO);
            record.setName(newName);
            catalogTableMapper.updateById(record);
            sqlSession.commit();
        }
    }

    @Override
    public void deleteView(CatalogType type, String catalog, String database, String viewName) throws TableNotExistException {
        try (SqlSession sqlSession = sqlSessionFactory.openSession()) {
            CatalogTableMapper catalogTableMapper = sqlSession.getMapper(CatalogTableMapper.class);
            if (!viewExists(type, catalog, database, viewName)) {
                throw new TableNotExistException(catalog, new ObjectPath(database, viewName));
            }
            catalogTableMapper.deleteByName(type, catalog, database, CatalogTableKind.VIEW, viewName);
            sqlSession.commit();
        }
    }

    @Override
    public List<CatalogFunctionDTO> listFunctions(CatalogType type, String catalog, String database) {
        try (SqlSession sqlSession = sqlSessionFactory.openSession()) {
            CatalogFunctionMapper catalogFunctionMapper = sqlSession.getMapper(CatalogFunctionMapper.class);
            List<CatalogFunction> catalogFunctions = catalogFunctionMapper.selectByDatabase(type, catalog, database);
            return CatalogFunctionConvert.INSTANCE.toDto(catalogFunctions);
        }
    }

    @Override
    public Optional<CatalogFunctionDTO> getFunction(CatalogType type, String catalog, String database, String function) {
        try (SqlSession sqlSession = sqlSessionFactory.openSession()) {
            CatalogFunctionMapper catalogFunctionMapper = sqlSession.getMapper(CatalogFunctionMapper.class);
            Optional<CatalogFunction> optional = catalogFunctionMapper.selectByName(type, catalog, database, function);
            return optional.map(record -> CatalogFunctionConvert.INSTANCE.toDto(record));
        }
    }

    @Override
    public boolean functionExists(CatalogType type, String catalog, String database, String function) {
        Optional<CatalogFunctionDTO> optional = getFunction(type, catalog, database, function);
        return optional.isPresent();
    }

    @Override
    public void insertFunction(CatalogType type, String catalog, String database, CatalogFunctionDTO function) throws DatabaseNotExistException, FunctionAlreadyExistException {
        try (SqlSession sqlSession = sqlSessionFactory.openSession()) {
            CatalogFunctionMapper catalogFunctionMapper = sqlSession.getMapper(CatalogFunctionMapper.class);
            CatalogDatabaseDTO catalogDatabaseDTO = getDatabase(type, catalog, database).orElseThrow(() -> new DatabaseNotExistException(catalog, database));
            if (functionExists(type, catalog, database, function.getName())) {
                throw new FunctionAlreadyExistException(catalog, new ObjectPath(database, function.getName()));
            }
            CatalogFunction record = CatalogFunctionConvert.INSTANCE.toDo(function);
            record.setDatabaseId(catalogDatabaseDTO.getId());
            catalogFunctionMapper.insert(record);
            sqlSession.commit();
        }
    }

    @Override
    public void updateFunction(CatalogType type, String catalog, String database, CatalogFunctionDTO function) throws FunctionNotExistException {
        try (SqlSession sqlSession = sqlSessionFactory.openSession()) {
            CatalogFunctionMapper catalogFunctionMapper = sqlSession.getMapper(CatalogFunctionMapper.class);
            if (functionExists(type, catalog, database, function.getName()) == false) {
                throw new FunctionNotExistException(catalog, new ObjectPath(database, function.getName()));
            }
            CatalogFunction record = CatalogFunctionConvert.INSTANCE.toDo(function);
            catalogFunctionMapper.updateById(record);
            sqlSession.commit();
        }
    }

    @Override
    public void deleteFunction(CatalogType type, String catalog, String database, String functionName) throws FunctionNotExistException {
        try (SqlSession sqlSession = sqlSessionFactory.openSession()) {
            CatalogFunctionMapper catalogFunctionMapper = sqlSession.getMapper(CatalogFunctionMapper.class);
            if (!functionExists(type, catalog, database, functionName)) {
                throw new FunctionNotExistException(catalog, new ObjectPath(database, functionName));
            }
            catalogFunctionMapper.deleteByName(type, catalog, database, functionName);
            sqlSession.commit();
        }
    }
}
