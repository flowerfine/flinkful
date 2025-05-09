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
package cn.sliew.flinkful.sql.catalog.service;

import cn.sliew.flinkful.sql.catalog.dict.CatalogType;
import cn.sliew.flinkful.sql.catalog.service.dto.CatalogDatabaseDTO;
import cn.sliew.flinkful.sql.catalog.service.dto.CatalogFunctionDTO;
import cn.sliew.flinkful.sql.catalog.service.dto.CatalogTableDTO;
import org.apache.flink.table.catalog.exceptions.*;

import java.util.List;
import java.util.Optional;

public interface FlinkfulCatalogService {

    List<CatalogDatabaseDTO> listDatabases(CatalogType type, String catalog);

    Optional<CatalogDatabaseDTO> getDatabase(CatalogType type, String catalog, String database);

    boolean databaseExists(CatalogType type, String catalog, String database);

    void insertDatabase(CatalogDatabaseDTO database) throws DatabaseAlreadyExistException;

    void updateDatabase(CatalogDatabaseDTO database) throws DatabaseNotExistException;

    void deleteDatabase(CatalogType type, String catalog, String database) throws DatabaseNotExistException;

    boolean isDatabaseEmpty(CatalogType type, String catalog, String database);

    List<CatalogTableDTO> listTables(CatalogType type, String catalog, String database);

    Optional<CatalogTableDTO> getTable(CatalogType type, String catalog, String database, String table);

    boolean tableExists(CatalogType type, String catalog, String database, String table);

    void insertTable(CatalogType type, String catalog, String database, CatalogTableDTO table) throws DatabaseNotExistException, TableAlreadyExistException;

    void updateTable(CatalogType type, String catalog, String database, CatalogTableDTO table) throws TableNotExistException;

    void renameTable(CatalogType type, String catalog, String database, String currentName, String newName) throws TableAlreadyExistException, TableNotExistException;

    void deleteTable(CatalogType type, String catalog, String database, String table) throws TableNotExistException;

    List<CatalogTableDTO> listViews(CatalogType type, String catalog, String database);

    Optional<CatalogTableDTO> getView(CatalogType type, String catalog, String database, String view);

    boolean viewExists(CatalogType type, String catalog, String database, String view);

    void insertView(CatalogType type, String catalog, String database, CatalogTableDTO view) throws DatabaseNotExistException, TableAlreadyExistException;

    void updateView(CatalogType type, String catalog, String database, CatalogTableDTO view) throws TableNotExistException;

    void renameView(CatalogType type, String catalog, String database, String currentName, String newName) throws TableNotExistException, TableAlreadyExistException;

    void deleteView(CatalogType type, String catalog, String database, String viewName) throws TableNotExistException;

    List<CatalogFunctionDTO> listFunctions(CatalogType type, String catalog, String database);

    Optional<CatalogFunctionDTO> getFunction(CatalogType type, String catalog, String database, String function);

    boolean functionExists(CatalogType type, String catalog, String database, String function);

    void insertFunction(CatalogType type, String catalog, String database, CatalogFunctionDTO function) throws DatabaseNotExistException, FunctionAlreadyExistException;

    void updateFunction(CatalogType type, String catalog, String database, CatalogFunctionDTO function) throws FunctionNotExistException;

    void deleteFunction(CatalogType type, String catalog, String database, String functionName) throws FunctionNotExistException;
}
