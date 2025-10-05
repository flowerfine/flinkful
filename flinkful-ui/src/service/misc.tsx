import createRequest from "./base";

const systemStop = createRequest<void, void>("/api/system/stop", {
  errorLevel: false,
  method: "post",
});
const testApiSmooth = createRequest<void, void>("/api/system/get-version-a", {
  errorLevel: false,
  method: "get",
});

export default {
  testService: async () => {
    return fetch("/data/system.json").then((response) => response.json());
  },
  systemStop,
  testApiSmooth,
};
