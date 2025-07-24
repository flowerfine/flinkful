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
package cn.sliew.flinkful.sql.gateway.embedded.service;

import cn.sliew.flinkful.sql.gateway.embedded.service.dto.CatalogInfo;
import cn.sliew.flinkful.sql.gateway.embedded.service.param.WsFlinkSqlGatewayQueryParam;
import org.apache.flink.table.gateway.api.results.ResultSet;

import java.util.Set;

public interface FlinkfulSqlGatewayService {

    Set<CatalogInfo> getCatalogInfo();

    String executeSql(WsFlinkSqlGatewayQueryParam params);

    ResultSet fetchResults(String operationHandleId, Long token, int maxRows);

    Boolean cancel(String operationHandleId);
}
