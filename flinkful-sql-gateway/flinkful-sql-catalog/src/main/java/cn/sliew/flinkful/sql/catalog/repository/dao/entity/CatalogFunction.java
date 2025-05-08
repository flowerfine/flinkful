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
package cn.sliew.flinkful.sql.catalog.repository.dao.entity;

import cn.sliew.carp.framework.mybatis.entity.BaseAuditDO;
import cn.sliew.flinkful.sql.catalog.common.dict.catalog.flink.CatalogFunctionLanguage;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("catalog_function")
public class CatalogFunction extends BaseAuditDO {

    private static final long serialVersionUID = 1L;

    @TableField("database_id")
    private Long databaseId;

    @TableField("`name`")
    private String name;

    @TableField("class_name")
    private String className;

    @TableField("function_language")
    private CatalogFunctionLanguage functionLanguage;

    @TableField("remark")
    private String remark;
}
