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
package cn.sliew.flinkful.sql.catalog.sakura.catalog.service.dto;

import cn.sliew.carp.framework.common.model.BaseDTO;
import cn.sliew.flinkful.sql.catalog.common.dict.catalog.flink.CatalogTableKind;
import lombok.Data;

import java.util.Map;

/**
 * @see org.apache.flink.table.catalog.DefaultCatalogTable
 * org.apache.flink.table.catalog.DefaultCatalogView
 */
@Data
public class CatalogTableDTO extends BaseDTO {

    private CatalogTableKind kind;
    private String name;
    private SchemaDTO schema;
    private Map<String, String> properties;
    private String originalQuery;
    private String expandedQuery;
    private String remark;
}