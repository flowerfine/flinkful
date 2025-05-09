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
package cn.sliew.flinkful.sql.catalog.service.impl;

import cn.sliew.flinkful.sql.catalog.dict.CatalogType;
import cn.sliew.flinkful.sql.catalog.dict.flink.CatalogTableKind;
import cn.sliew.flinkful.sql.catalog.repository.entity.CatalogDatabase;
import cn.sliew.flinkful.sql.catalog.repository.entity.CatalogFunction;
import cn.sliew.flinkful.sql.catalog.repository.entity.CatalogTable;
import cn.sliew.flinkful.sql.catalog.repository.mapper.CatalogDatabaseMapper;
import cn.sliew.flinkful.sql.catalog.repository.mapper.CatalogFunctionMapper;
import cn.sliew.flinkful.sql.catalog.repository.mapper.CatalogTableMapper;
import cn.sliew.flinkful.sql.catalog.service.FlinkfulCatalogService;
import cn.sliew.flinkful.sql.catalog.service.convert.CatalogDatabaseConvert;
import cn.sliew.flinkful.sql.catalog.service.convert.CatalogFunctionConvert;
import cn.sliew.flinkful.sql.catalog.service.convert.CatalogTableConvert;
import cn.sliew.flinkful.sql.catalog.service.dto.CatalogDatabaseDTO;
import cn.sliew.flinkful.sql.catalog.service.dto.CatalogFunctionDTO;
import cn.sliew.flinkful.sql.catalog.service.dto.CatalogTableDTO;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import lombok.RequiredArgsConstructor;
import org.apache.flink.table.catalog.ObjectPath;
import org.apache.flink.table.catalog.exceptions.*;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class FlinkfulCatalogServiceImpl implements FlinkfulCatalogService {

    private final CatalogDatabaseMapper catalogDatabaseMapper;
    private final CatalogTableMapper catalogTableMapper;
    private final CatalogFunctionMapper catalogFunctionMapper;

    @Override
    public List<CatalogDatabaseDTO> listDatabases(CatalogType type, String catalog) {
        LambdaQueryWrapper<CatalogDatabase> queryWrapper = Wrappers.lambdaQuery(CatalogDatabase.class)
                .eq(CatalogDatabase::getType, type)
                .eq(CatalogDatabase::getCatalog, catalog)
                .orderByAsc(CatalogDatabase::getName);
        List<CatalogDatabase> databases = catalogDatabaseMapper.selectList(queryWrapper);
        return CatalogDatabaseConvert.INSTANCE.toDto(databases);
    }

    @Override
    public Optional<CatalogDatabaseDTO> getDatabase(CatalogType type, String catalog, String database) {
        LambdaQueryWrapper<CatalogDatabase> queryWrapper = Wrappers.lambdaQuery(CatalogDatabase.class)
                .eq(CatalogDatabase::getCatalog, catalog)
                .eq(CatalogDatabase::getName, database);
        CatalogDatabase record = catalogDatabaseMapper.selectOne(queryWrapper);
        return Optional.ofNullable(record).map(CatalogDatabaseConvert.INSTANCE::toDto);
    }

    @Override
    public boolean databaseExists(CatalogType type, String catalog, String database) {
        Optional<CatalogDatabaseDTO> optional = getDatabase(type, catalog, database);
        return optional.isPresent();
    }

    @Override
    public void insertDatabase(CatalogDatabaseDTO database) throws DatabaseAlreadyExistException {
        if (databaseExists(database.getType(), database.getCatalog(), database.getName())) {
            throw new DatabaseAlreadyExistException(database.getCatalog(), database.getName());
        }
        CatalogDatabase record = CatalogDatabaseConvert.INSTANCE.toDo(database);
        catalogDatabaseMapper.insert(record);
    }

    @Override
    public void updateDatabase(CatalogDatabaseDTO database) throws DatabaseNotExistException {
        if (!databaseExists(database.getType(), database.getCatalog(), database.getName())) {
            throw new DatabaseNotExistException(database.getCatalog(), database.getName());
        }
        CatalogDatabase record = CatalogDatabaseConvert.INSTANCE.toDo(database);
        catalogDatabaseMapper.updateById(record);
    }

    @Override
    public void deleteDatabase(CatalogType type, String catalog, String database) throws DatabaseNotExistException {
        if (!databaseExists(type, catalog, database)) {
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
    }

    @Override
    public boolean isDatabaseEmpty(CatalogType type, String catalog, String database) {
        int tableCount = catalogTableMapper.countByDatabase(type, catalog, database, CatalogTableKind.TABLE);
        int functionCount = catalogFunctionMapper.countByDatabase(type, catalog, database);
        return tableCount != 0 || functionCount != 0;
    }

    @Override
    public List<CatalogTableDTO> listTables(CatalogType type, String catalog, String database) {
        List<CatalogTable> records = catalogTableMapper.selectByDatabase(type, catalog, database, CatalogTableKind.TABLE);
        return CatalogTableConvert.INSTANCE.toDto(records);
    }

    @Override
    public Optional<CatalogTableDTO> getTable(CatalogType type, String catalog, String database, String table) {
        Optional<CatalogTable> optional = catalogTableMapper.selectByName(type, catalog, database, CatalogTableKind.TABLE, table);
        return optional.map(CatalogTableConvert.INSTANCE::toDto);
    }

    @Override
    public boolean tableExists(CatalogType type, String catalog, String database, String table) {
        Optional<CatalogTableDTO> optional = getTable(type, catalog, database, table);
        return optional.isPresent();
    }

    @Override
    public void insertTable(CatalogType type, String catalog, String database, CatalogTableDTO table) throws DatabaseNotExistException, TableAlreadyExistException {
        CatalogDatabaseDTO catalogDatabaseDTO = getDatabase(type, catalog, database).orElseThrow(() -> new DatabaseNotExistException(catalog, database));
        if (tableExists(type, catalog, database, table.getName())) {
            throw new TableAlreadyExistException(catalog, new ObjectPath(database, table.getName()));
        }
        CatalogTable record = CatalogTableConvert.INSTANCE.toDo(table);
        record.setDatabaseId(catalogDatabaseDTO.getId());
        catalogTableMapper.insert(record);
    }

    @Override
    public void updateTable(CatalogType type, String catalog, String database, CatalogTableDTO table) throws TableNotExistException {
        if (!tableExists(type, catalog, database, table.getName())) {
            throw new TableNotExistException(catalog, new ObjectPath(database, table.getName()));
        }
        CatalogTable record = CatalogTableConvert.INSTANCE.toDo(table);
        catalogTableMapper.updateById(record);
    }

    @Override
    public void renameTable(CatalogType type, String catalog, String database, String currentName, String newName) throws TableAlreadyExistException, TableNotExistException {
        CatalogTableDTO catalogTableDTO = getTable(type, catalog, database, currentName).orElseThrow(() -> new TableNotExistException(catalog, new ObjectPath(database, currentName)));
        if (tableExists(type, catalog, database, newName)) {
            throw new TableAlreadyExistException(catalog, new ObjectPath(database, newName));
        }
        CatalogTable record = CatalogTableConvert.INSTANCE.toDo(catalogTableDTO);
        record.setName(newName);
        catalogTableMapper.updateById(record);
    }

    @Override
    public void deleteTable(CatalogType type, String catalog, String database, String table) throws TableNotExistException {
        if (!tableExists(type, catalog, database, table)) {
            throw new TableNotExistException(catalog, new ObjectPath(database, table));
        }
        catalogTableMapper.deleteByName(type, catalog, database, CatalogTableKind.TABLE, table);
    }

    @Override
    public List<CatalogTableDTO> listViews(CatalogType type, String catalog, String database) {
        List<CatalogTable> views = catalogTableMapper.selectByDatabase(type, catalog, database, CatalogTableKind.VIEW);
        return CatalogTableConvert.INSTANCE.toDto(views);
    }

    @Override
    public Optional<CatalogTableDTO> getView(CatalogType type, String catalog, String database, String view) {
        Optional<CatalogTable> optional = catalogTableMapper.selectByName(type, catalog, database, CatalogTableKind.VIEW, view);
        return optional.map(CatalogTableConvert.INSTANCE::toDto);
    }

    @Override
    public boolean viewExists(CatalogType type, String catalog, String database, String view) {
        Optional<CatalogTableDTO> optional = getView(type, catalog, database, view);
        return optional.isPresent();
    }

    @Override
    public void insertView(CatalogType type, String catalog, String database, CatalogTableDTO view) throws DatabaseNotExistException, TableAlreadyExistException {
        CatalogDatabaseDTO catalogDatabaseDTO = getDatabase(type, catalog, database).orElseThrow(() -> new DatabaseNotExistException(catalog, database));
        if (viewExists(type, catalog, database, view.getName())) {
            throw new TableAlreadyExistException(catalog, new ObjectPath(database, view.getName()));
        }
        CatalogTable record = CatalogTableConvert.INSTANCE.toDo(view);
        record.setDatabaseId(catalogDatabaseDTO.getId());
        catalogTableMapper.insert(record);
    }

    @Override
    public void updateView(CatalogType type, String catalog, String database, CatalogTableDTO view) throws TableNotExistException {
        if (!viewExists(type, catalog, database, view.getName())) {
            throw new TableNotExistException(catalog, new ObjectPath(database, view.getName()));
        }
        CatalogTable record = CatalogTableConvert.INSTANCE.toDo(view);
        catalogTableMapper.updateById(record);
    }

    @Override
    public void renameView(CatalogType type, String catalog, String database, String currentName, String newName) throws TableNotExistException, TableAlreadyExistException {
        CatalogTableDTO catalogViewDTO = getView(type, catalog, database, currentName).orElseThrow(() -> new TableNotExistException(catalog, new ObjectPath(database, currentName)));
        if (viewExists(type, catalog, database, newName)) {
            throw new TableAlreadyExistException(catalog, new ObjectPath(database, newName));
        }
        CatalogTable record = CatalogTableConvert.INSTANCE.toDo(catalogViewDTO);
        record.setName(newName);
        catalogTableMapper.updateById(record);
    }

    @Override
    public void deleteView(CatalogType type, String catalog, String database, String viewName) throws TableNotExistException {
        if (!viewExists(type, catalog, database, viewName)) {
            throw new TableNotExistException(catalog, new ObjectPath(database, viewName));
        }
        catalogTableMapper.deleteByName(type, catalog, database, CatalogTableKind.VIEW, viewName);
    }

    @Override
    public List<CatalogFunctionDTO> listFunctions(CatalogType type, String catalog, String database) {
        List<CatalogFunction> catalogFunctions = catalogFunctionMapper.selectByDatabase(type, catalog, database);
        return CatalogFunctionConvert.INSTANCE.toDto(catalogFunctions);
    }

    @Override
    public Optional<CatalogFunctionDTO> getFunction(CatalogType type, String catalog, String database, String function) {
        Optional<CatalogFunction> optional = catalogFunctionMapper.selectByName(type, catalog, database, function);
        return optional.map(CatalogFunctionConvert.INSTANCE::toDto);
    }

    @Override
    public boolean functionExists(CatalogType type, String catalog, String database, String function) {
        Optional<CatalogFunctionDTO> optional = getFunction(type, catalog, database, function);
        return optional.isPresent();
    }

    @Override
    public void insertFunction(CatalogType type, String catalog, String database, CatalogFunctionDTO function) throws DatabaseNotExistException, FunctionAlreadyExistException {
        CatalogDatabaseDTO catalogDatabaseDTO = getDatabase(type, catalog, database).orElseThrow(() -> new DatabaseNotExistException(catalog, database));
        if (functionExists(type, catalog, database, function.getName())) {
            throw new FunctionAlreadyExistException(catalog, new ObjectPath(database, function.getName()));
        }
        CatalogFunction record = CatalogFunctionConvert.INSTANCE.toDo(function);
        record.setDatabaseId(catalogDatabaseDTO.getId());
        catalogFunctionMapper.insert(record);
    }

    @Override
    public void updateFunction(CatalogType type, String catalog, String database, CatalogFunctionDTO function) throws FunctionNotExistException {
        if (!functionExists(type, catalog, database, function.getName())) {
            throw new FunctionNotExistException(catalog, new ObjectPath(database, function.getName()));
        }
        CatalogFunction record = CatalogFunctionConvert.INSTANCE.toDo(function);
        catalogFunctionMapper.updateById(record);
    }

    @Override
    public void deleteFunction(CatalogType type, String catalog, String database, String functionName) throws FunctionNotExistException {
        if (!functionExists(type, catalog, database, functionName)) {
            throw new FunctionNotExistException(catalog, new ObjectPath(database, functionName));
        }
        catalogFunctionMapper.deleteByName(type, catalog, database, functionName);
    }
}
