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
import cn.sliew.flinkful.sql.catalog.repository.dao.entity.CatalogStore;
import cn.sliew.flinkful.sql.catalog.repository.dao.mapper.CatalogStoreMapper;
import cn.sliew.flinkful.sql.catalog.sakura.catalog.service.CatalogStoreService;
import cn.sliew.flinkful.sql.catalog.sakura.catalog.service.convert.CatalogStoreConvert;
import cn.sliew.flinkful.sql.catalog.sakura.catalog.service.dto.CatalogStoreDTO;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import java.util.List;
import java.util.Optional;

public class CatalogStoreServiceImpl implements CatalogStoreService {

    private final SqlSessionFactory sqlSessionFactory;

    public CatalogStoreServiceImpl(SqlSessionFactory sqlSessionFactory) {
        this.sqlSessionFactory = sqlSessionFactory;
    }

    @Override
    public List<CatalogStoreDTO> list(CatalogType type) {
        try (SqlSession sqlSession = sqlSessionFactory.openSession()) {
            CatalogStoreMapper catalogStoreMapper = sqlSession.getMapper(CatalogStoreMapper.class);
            LambdaQueryWrapper<CatalogStore> queryWrapper = Wrappers.lambdaQuery(CatalogStore.class)
                    .eq(CatalogStore::getType, type)
                    .orderByAsc(CatalogStore::getCatalogName);
            List<CatalogStore> catalogs = catalogStoreMapper.selectList(queryWrapper);
            return CatalogStoreConvert.INSTANCE.toDto(catalogs);
        }
    }

    @Override
    public Optional<CatalogStoreDTO> get(CatalogType type, String catalogName) {
        try (SqlSession sqlSession = sqlSessionFactory.openSession()) {
            CatalogStoreMapper catalogStoreMapper = sqlSession.getMapper(CatalogStoreMapper.class);
            LambdaQueryWrapper<CatalogStore> queryWrapper = Wrappers.lambdaQuery(CatalogStore.class)
                    .eq(CatalogStore::getType, type)
                    .eq(CatalogStore::getCatalogName, catalogName);
            CatalogStore catalog = catalogStoreMapper.selectOne(queryWrapper);
            return Optional.ofNullable(catalog).map(CatalogStoreConvert.INSTANCE::toDto);
        }
    }

    @Override
    public void insert(CatalogStoreDTO dto) {
        try (SqlSession sqlSession = sqlSessionFactory.openSession()) {
            CatalogStoreMapper catalogStoreMapper = sqlSession.getMapper(CatalogStoreMapper.class);
            CatalogStore record = CatalogStoreConvert.INSTANCE.toDo(dto);
            catalogStoreMapper.insert(record);
            sqlSession.commit();
        }
    }

    @Override
    public void delete(CatalogType type, String catalogName) {
        try (SqlSession sqlSession = sqlSessionFactory.openSession()) {
            CatalogStoreMapper catalogStoreMapper = sqlSession.getMapper(CatalogStoreMapper.class);
            LambdaUpdateWrapper<CatalogStore> updateWrapper = Wrappers.lambdaUpdate(CatalogStore.class)
                    .eq(CatalogStore::getType, type)
                    .eq(CatalogStore::getCatalogName, catalogName);
            CatalogStore record = new CatalogStore();
            record.setType(type);
            record.setCatalogName(catalogName);
            catalogStoreMapper.update(record, updateWrapper);
            sqlSession.commit();
        }
    }
}
