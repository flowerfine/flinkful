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
import cn.sliew.flinkful.sql.catalog.repository.entity.CatalogStore;
import cn.sliew.flinkful.sql.catalog.service.dto.CatalogStoreDTO;
import cn.sliew.milky.common.exception.Rethrower;
import cn.sliew.milky.common.util.JacksonUtil;
import com.fasterxml.jackson.core.type.TypeReference;
import org.apache.commons.lang3.StringUtils;
import org.apache.flink.configuration.Configuration;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.BeanUtils;

import java.util.Map;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CatalogStoreConvert extends BaseConvert<CatalogStore, CatalogStoreDTO> {
    CatalogStoreConvert INSTANCE = Mappers.getMapper(CatalogStoreConvert.class);

    @Override
    default CatalogStore toDo(CatalogStoreDTO dto) {
        try {
            CatalogStore entity = new CatalogStore();
            BeanUtils.copyProperties(dto, entity);
            entity.setName(dto.getCatalogName());
            if (dto.getConfiguration() != null) {
                entity.setConfiguration(CodecUtil.decrypt(JacksonUtil.toJsonString(dto.getConfiguration())));
            }
            return entity;
        } catch (Exception e) {
            Rethrower.throwAs(e);
            return null;
        }
    }

    @Override
    default CatalogStoreDTO toDto(CatalogStore entity) {
        try {
            CatalogStoreDTO dto = new CatalogStoreDTO();
            BeanUtils.copyProperties(entity, dto);
            dto.setCatalogName(entity.getName());
            if (entity != null && StringUtils.isNotBlank(entity.getConfiguration())) {
                Map<String, String> configuration = JacksonUtil.parseJsonString(CodecUtil.decrypt(entity.getConfiguration()), new TypeReference<Map<String, String>>() {
                });
                dto.setConfiguration(Configuration.fromMap(configuration));
            }
            return dto;
        } catch (Exception e) {
            Rethrower.throwAs(e);
            return null;
        }
    }
}
