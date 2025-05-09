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
package cn.sliew.flinkful.sql.catalog.service.convert;

import cn.sliew.carp.framework.common.codec.CodecUtil;
import cn.sliew.carp.framework.common.convert.BaseConvert;
import cn.sliew.flinkful.sql.catalog.repository.entity.CatalogDatabase;
import cn.sliew.flinkful.sql.catalog.service.dto.CatalogDatabaseDTO;
import cn.sliew.milky.common.exception.Rethrower;
import cn.sliew.milky.common.util.JacksonUtil;
import com.fasterxml.jackson.core.type.TypeReference;
import org.apache.commons.lang3.StringUtils;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.BeanUtils;

import java.util.Map;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CatalogDatabaseConvert extends BaseConvert<CatalogDatabase, CatalogDatabaseDTO> {
    CatalogDatabaseConvert INSTANCE = Mappers.getMapper(CatalogDatabaseConvert.class);

    @Override
    default CatalogDatabase toDo(CatalogDatabaseDTO dto) {
        try {
            CatalogDatabase entity = new CatalogDatabase();
            BeanUtils.copyProperties(dto, entity);
            entity.setCatalog(dto.getCatalog());
            entity.setName(dto.getName());
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
    default CatalogDatabaseDTO toDto(CatalogDatabase entity) {
        try {
            CatalogDatabaseDTO dto = new CatalogDatabaseDTO();
            BeanUtils.copyProperties(entity, dto);
            dto.setCatalog(entity.getCatalog());
            dto.setName(entity.getName());
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
