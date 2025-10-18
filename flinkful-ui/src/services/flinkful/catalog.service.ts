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
};
