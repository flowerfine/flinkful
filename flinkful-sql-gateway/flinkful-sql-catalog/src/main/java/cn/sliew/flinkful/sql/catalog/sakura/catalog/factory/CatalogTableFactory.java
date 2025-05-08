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

package cn.sliew.flinkful.sql.catalog.sakura.catalog.factory;

import cn.sliew.sakura.catalog.SakuraCatalogTable;
import cn.sliew.sakura.catalog.service.dto.CatalogTableDTO;
import cn.sliew.sakura.common.dict.catalog.flink.CatalogTableKind;
import org.apache.commons.lang3.EnumUtils;
import org.apache.flink.table.api.Schema;
import org.apache.flink.table.catalog.CatalogBaseTable;
import org.apache.flink.table.catalog.ResolvedCatalogTable;

public enum CatalogTableFactory {
    ;

    public static CatalogTableDTO fromResolvedTable(String tableName, CatalogBaseTable catalogBaseTable) {
        CatalogTableDTO catalogTable = new CatalogTableDTO();
        catalogTable.setKind(CatalogTableKind.TABLE);
        catalogTable.setName(tableName);
        catalogTable.setProperties(catalogBaseTable.getOptions());
        catalogTable.setRemark(catalogBaseTable.getComment());
        if (catalogBaseTable instanceof ResolvedCatalogTable) {
            ResolvedCatalogTable resolvedTable = (ResolvedCatalogTable) catalogBaseTable;
            catalogTable.setSchema(CatalogSchemaFactory.toSchema(resolvedTable.getResolvedSchema()));
        }
        return catalogTable;
    }

    public static CatalogTableDTO fromUnresolvedTable(String tableName, CatalogBaseTable catalogBaseTable) {
        CatalogTableDTO catalogTable = new CatalogTableDTO();
        catalogTable.setKind(CatalogTableKind.TABLE);
        catalogTable.setName(tableName);
        catalogTable.setProperties(catalogBaseTable.getOptions());
        catalogTable.setRemark(catalogBaseTable.getComment());
        catalogTable.setSchema(CatalogSchemaFactory.toSchema(catalogBaseTable.getUnresolvedSchema()));
        return catalogTable;
    }

    public static SakuraCatalogTable toTable(CatalogTableDTO catalogTableDTO) {
        CatalogBaseTable.TableKind kind = EnumUtils.getEnum(CatalogBaseTable.TableKind.class, catalogTableDTO.getKind().getValue());
        Schema schema = CatalogSchemaFactory.toCatalog(catalogTableDTO.getSchema());
        return new SakuraCatalogTable(kind, schema, catalogTableDTO.getProperties(), catalogTableDTO.getRemark());
    }
}
