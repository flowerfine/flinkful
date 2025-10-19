import { request } from "@umijs/max";

export const CatalogService = {
  url: "/flinkful/gateway",

  getCatalogInfo: async () => {
    return request<Array<CatalogAPI.Catalog>>(
      `${CatalogService.url}/getCatalogInfo`,
      {
        method: "GET",
      }
    );
  },

  listCatalogs: async () => {
    return request<Array<string>>(`${CatalogService.url}/catalogs`, {
      method: "GET",
    });
  },

  listDatabases: async (catalogName: string) => {
    return request<Array<string>>(
      `${CatalogService.url}/catalogs/${catalogName}/databases`,
      {
        method: "GET",
      }
    );
  },

  listTables: async (catalogName: string, databaseName: string) => {
    return request<Array<CatalogAPI.Table>>(
      `${CatalogService.url}/catalogs/${catalogName}/databases/${databaseName}/tables`,
      {
        method: "GET",
      }
    );
  },

  listViews: async (catalogName: string, databaseName: string) => {
    return request<Array<CatalogAPI.Table>>(
      `${CatalogService.url}/catalogs/${catalogName}/databases/${databaseName}/views`,
      {
        method: "GET",
      }
    );
  },

  listUdfs: async (catalogName: string, databaseName: string) => {
    return request<Array<CatalogAPI.Function>>(
      `${CatalogService.url}/catalogs/${catalogName}/databases/${databaseName}/udfs`,
      {
        method: "GET",
      }
    );
  },
};
