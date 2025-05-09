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
import cn.sliew.flinkful.sql.catalog.repository.entity.CatalogStore;
import cn.sliew.flinkful.sql.catalog.repository.mapper.CatalogStoreMapper;
import cn.sliew.flinkful.sql.catalog.service.FlinkfulCatalogStoreService;
import cn.sliew.flinkful.sql.catalog.service.convert.CatalogStoreConvert;
import cn.sliew.flinkful.sql.catalog.service.dto.CatalogStoreDTO;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class FlinkfulCatalogStoreServiceImpl extends ServiceImpl<CatalogStoreMapper, CatalogStore> implements FlinkfulCatalogStoreService {

    @Override
    public List<CatalogStoreDTO> list(CatalogType type) {
        LambdaQueryWrapper<CatalogStore> queryWrapper = Wrappers.lambdaQuery(CatalogStore.class)
                .eq(CatalogStore::getType, type)
                .orderByAsc(CatalogStore::getName);
        List<CatalogStore> catalogs = list(queryWrapper);
        return CatalogStoreConvert.INSTANCE.toDto(catalogs);
    }

    @Override
    public Optional<CatalogStoreDTO> get(CatalogType type, String catalogName) {
        LambdaQueryWrapper<CatalogStore> queryWrapper = Wrappers.lambdaQuery(CatalogStore.class)
                .eq(CatalogStore::getType, type)
                .eq(CatalogStore::getName, catalogName);
        Optional<CatalogStore> catalog = getOneOpt(queryWrapper);
        return catalog.map(CatalogStoreConvert.INSTANCE::toDto);
    }

    @Override
    public void add(CatalogStoreDTO dto) {
        CatalogStore record = CatalogStoreConvert.INSTANCE.toDo(dto);
        save(record);
    }

    @Override
    public void update(CatalogStoreDTO dto) {
        CatalogStore record = CatalogStoreConvert.INSTANCE.toDo(dto);
        LambdaUpdateWrapper<CatalogStore> updateWrapper = Wrappers.lambdaUpdate(CatalogStore.class)
                .eq(CatalogStore::getType, record.getType())
                .eq(CatalogStore::getName, record.getName());
        update(record, updateWrapper);
    }

    @Override
    public void delete(CatalogType type, String catalogName) {
        LambdaUpdateWrapper<CatalogStore> updateWrapper = Wrappers.lambdaUpdate(CatalogStore.class)
                .eq(CatalogStore::getType, type)
                .eq(CatalogStore::getName, catalogName);
        remove(updateWrapper);
    }
}
