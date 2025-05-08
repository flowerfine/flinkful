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

import cn.sliew.carp.framework.common.codec.CodecUtil;
import cn.sliew.carp.framework.common.convert.BaseConvert;
import cn.sliew.flinkful.sql.catalog.repository.dao.entity.CatalogTable;
import cn.sliew.flinkful.sql.catalog.sakura.catalog.service.dto.CatalogTableDTO;
import cn.sliew.flinkful.sql.catalog.sakura.catalog.service.dto.SchemaDTO;
import cn.sliew.milky.common.exception.Rethrower;
import cn.sliew.milky.common.util.JacksonUtil;
import com.fasterxml.jackson.core.type.TypeReference;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;

public enum CatalogTableConvert implements BaseConvert<CatalogTable, CatalogTableDTO> {
    INSTANCE;

    @Override
    public CatalogTable toDo(CatalogTableDTO dto) {

        try {
            CatalogTable entity = new CatalogTable();
            Util.copyProperties(dto, entity);
            entity.setKind(dto.getKind());
            entity.setName(dto.getName());
            if (dto.getSchema() != null) {
                entity.setSchema(JacksonUtil.toJsonString(dto.getSchema()));
            }
            entity.setOriginalQuery(dto.getOriginalQuery());
            entity.setExpandedQuery(dto.getExpandedQuery());
            entity.setRemark(dto.getRemark());
            if (dto.getProperties() != null) {
                entity.setProperties(CodecUtil.encrypt(JacksonUtil.toJsonString(dto.getProperties())));
            }
            return entity;
        } catch (Exception e) {
            Rethrower.throwAs(e);
            return null;
        }
    }

    @Override
    public CatalogTableDTO toDto(CatalogTable entity) {
        try {
            CatalogTableDTO dto = new CatalogTableDTO();
            Util.copyProperties(entity, dto);
            dto.setKind(entity.getKind());
            dto.setName(entity.getName());
            if (StringUtils.isNotBlank(entity.getSchema())) {
                dto.setSchema(JacksonUtil.parseJsonString(entity.getSchema(), SchemaDTO.class));
            }
            dto.setOriginalQuery(entity.getOriginalQuery());
            dto.setExpandedQuery(entity.getExpandedQuery());
            dto.setRemark(entity.getRemark());
            if (entity != null && StringUtils.isNotBlank(entity.getProperties())) {
                Map<String, String> properties = JacksonUtil.parseJsonString(CodecUtil.decrypt(entity.getProperties()), new TypeReference<Map<String, String>>() {
                });
                dto.setProperties(properties);
            }
            return dto;
        } catch (Exception e) {
            Rethrower.throwAs(e);
            return null;
        }
    }
}
