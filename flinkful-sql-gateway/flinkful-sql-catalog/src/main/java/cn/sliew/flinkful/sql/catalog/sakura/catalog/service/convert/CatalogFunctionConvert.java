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
package cn.sliew.flinkful.sql.catalog.sakura.catalog.service.convert;

import cn.sliew.carp.framework.common.convert.BaseConvert;
import cn.sliew.flinkful.sql.catalog.repository.dao.entity.CatalogFunction;
import cn.sliew.flinkful.sql.catalog.sakura.catalog.service.dto.CatalogFunctionDTO;
import cn.sliew.milky.common.exception.Rethrower;

public enum CatalogFunctionConvert implements BaseConvert<CatalogFunction, CatalogFunctionDTO> {
    INSTANCE;

    @Override
    public CatalogFunction toDo(CatalogFunctionDTO dto) {
        try {
            CatalogFunction entity = new CatalogFunction();
            Util.copyProperties(dto, entity);
            entity.setName(dto.getName());
            entity.setClassName(dto.getClassName());
            entity.setFunctionLanguage(dto.getFunctionLanguage());
            entity.setRemark(dto.getRemark());
            return entity;
        } catch (Exception e) {
            Rethrower.throwAs(e);
            return null;
        }
    }

    @Override
    public CatalogFunctionDTO toDto(CatalogFunction entity) {
        try {
            CatalogFunctionDTO dto = new CatalogFunctionDTO();
            Util.copyProperties(entity, dto);
            dto.setName(entity.getName());
            dto.setClassName(entity.getClassName());
            dto.setFunctionLanguage(entity.getFunctionLanguage());
            dto.setRemark(entity.getRemark());
            return dto;
        } catch (Exception e) {
            Rethrower.throwAs(e);
            return null;
        }
    }
}
