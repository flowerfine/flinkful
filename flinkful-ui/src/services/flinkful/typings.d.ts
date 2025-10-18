// @ts-ignore
/* eslint-disable */

declare namespace CatalogAPI {
  type Catalog = {
    name: string;
    type: string;
    databases: Array<Database>;
    systemFunctions: Array<Function>;
    properties: Record<string, any>;
  };

  type Database = {
    name: string;
    type: string;
    description: string;
    tables: Array<Table>;
    views: Array<Table>;
    userDefinedFunctions: Array<Function>;
    properties: Record<string, any>;
    comment: string;
  };

  type Table = {
    name: string;
    type: string;
    tableKind: string;
    schema: Array<Column>;
    description: string;
    properties: Record<string, any>;
    comment: string;
  };

  type Column = {
    columnName: string;
    dataType: string;
    isPersist: boolean;
    isPhysical: boolean;
    comment: string;
  };

  type Function = {
    name: string;
    type: string;
    functionKind: string;
    properties: Record<string, any>;
  };
}
