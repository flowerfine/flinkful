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
package cn.sliew.flinkful.sql.catalog.sakura.factory;

import cn.sliew.flinkful.sql.catalog.dict.flink.CatalogTableKind;
import cn.sliew.flinkful.sql.catalog.sakura.SakuraCatalogView;
import cn.sliew.flinkful.sql.catalog.service.dto.CatalogTableDTO;
import org.apache.commons.lang3.EnumUtils;
import org.apache.flink.table.api.Schema;
import org.apache.flink.table.catalog.CatalogBaseTable;
import org.apache.flink.table.catalog.ResolvedCatalogView;

public enum CatalogViewFactory {
    ;

    public static CatalogTableDTO fromResolvedView(String viewName, ResolvedCatalogView resolvedView) {
        CatalogTableDTO catalogView = new CatalogTableDTO();
        catalogView.setKind(CatalogTableKind.VIEW);
        catalogView.setName(viewName);
        catalogView.setSchema(CatalogSchemaFactory.toSchema(resolvedView.getResolvedSchema()));
        catalogView.setProperties(resolvedView.getOptions());
        catalogView.setOriginalQuery(resolvedView.getOriginalQuery());
        catalogView.setExpandedQuery(resolvedView.getExpandedQuery());
        catalogView.setRemark(resolvedView.getComment());
        return catalogView;
    }

    public static CatalogTableDTO fromUnresolvedTable(String viewName, CatalogBaseTable catalogBaseTable) {
        CatalogTableDTO catalogView = new CatalogTableDTO();
        catalogView.setKind(CatalogTableKind.VIEW);
        catalogView.setName(viewName);
        catalogView.setSchema(CatalogSchemaFactory.toSchema(catalogBaseTable.getUnresolvedSchema()));
        catalogView.setProperties(catalogBaseTable.getOptions());
        catalogView.setOriginalQuery(catalogView.getOriginalQuery());
        catalogView.setExpandedQuery(catalogView.getExpandedQuery());
        catalogView.setRemark(catalogBaseTable.getComment());
        return catalogView;
    }

    public static SakuraCatalogView toView(CatalogTableDTO catalogTableDTO) {
        CatalogBaseTable.TableKind kind = EnumUtils.getEnum(CatalogBaseTable.TableKind.class, catalogTableDTO.getKind().getValue());
        Schema schema = CatalogSchemaFactory.toCatalog(catalogTableDTO.getSchema());
        return new SakuraCatalogView(kind, schema, catalogTableDTO.getProperties(), catalogTableDTO.getRemark(), catalogTableDTO.getOriginalQuery(), catalogTableDTO.getExpandedQuery());
    }

}
