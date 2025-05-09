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
package cn.sliew.flinkful.sql.catalog.store;

import cn.hutool.extra.spring.SpringUtil;
import cn.sliew.flinkful.sql.catalog.dict.CatalogType;
import cn.sliew.flinkful.sql.catalog.service.FlinkfulCatalogStoreService;
import cn.sliew.flinkful.sql.catalog.service.dto.CatalogStoreDTO;
import org.apache.flink.table.catalog.AbstractCatalogStore;
import org.apache.flink.table.catalog.CatalogDescriptor;
import org.apache.flink.table.catalog.exceptions.CatalogException;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class JdbcCatalogStore extends AbstractCatalogStore {

    private FlinkfulCatalogStoreService catalogStoreService;

    @Override
    public void open() {
        super.open();
        this.catalogStoreService = SpringUtil.getBean(FlinkfulCatalogStoreService.class);
    }

    @Override
    public void storeCatalog(String catalogName, CatalogDescriptor catalog) throws CatalogException {
        CatalogStoreDTO dto = new CatalogStoreDTO();
        dto.setType(CatalogType.FLINK);
        dto.setCatalogName(catalog.getCatalogName());
        dto.setConfiguration(catalog.getConfiguration());
        if (contains(catalogName)) {
            catalogStoreService.update(dto);
        } else {
            catalogStoreService.add(dto);
        }
    }

    @Override
    public void removeCatalog(String catalogName, boolean ignoreIfNotExists) throws CatalogException {
        if (!contains(catalogName) && !ignoreIfNotExists) {
            throw new CatalogException(String.format("Catalog %s's store is not exist", catalogName));
        }

        catalogStoreService.delete(CatalogType.FLINK, catalogName);
    }

    @Override
    public Optional<CatalogDescriptor> getCatalog(String catalogName) throws CatalogException {
        return catalogStoreService.get(CatalogType.FLINK, catalogName)
                .map(dto -> CatalogDescriptor.of(dto.getCatalogName(), dto.getConfiguration()));
    }

    @Override
    public Set<String> listCatalogs() throws CatalogException {
        return catalogStoreService.list(CatalogType.FLINK).stream()
                .map(CatalogStoreDTO::getCatalogName)
                .collect(Collectors.toSet());
    }

    @Override
    public boolean contains(String catalogName) throws CatalogException {
        return getCatalog(catalogName).isPresent();
    }
}
