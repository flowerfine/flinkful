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
package cn.sliew.flinkful.sql.catalog.repository.mapper;

import cn.sliew.flinkful.sql.catalog.dict.CatalogType;
import cn.sliew.flinkful.sql.catalog.repository.entity.CatalogFunction;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Optional;

@Mapper
public interface CatalogFunctionMapper extends BaseMapper<CatalogFunction> {

    List<CatalogFunction> selectByDatabase(@Param("type") CatalogType type, @Param("catalog") String catalog, @Param("database") String database);

    int countByDatabase(@Param("type") CatalogType type, @Param("catalog") String catalog, @Param("database") String database);

    Optional<CatalogFunction> selectByName(@Param("type") CatalogType type, @Param("catalog") String catalog, @Param("database") String database, @Param("name") String name);

    int deleteByName(@Param("type") CatalogType type, @Param("catalog") String catalog, @Param("database") String database, @Param("name") String name);
}
