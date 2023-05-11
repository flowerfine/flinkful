package cn.sliew.flinkful.rest.http.v1;

import cn.sliew.flinkful.rest.base.v1.messages.DashboardConfiguration;
import feign.Headers;
import feign.RequestLine;

public interface DashboardClientV1 {

    @RequestLine("GET v1/config")
    @Headers("Content-Type: application/json")
    DashboardConfiguration config();
}
