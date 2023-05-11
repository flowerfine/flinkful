package cn.sliew.flinkful.rest.http.v1;

import cn.sliew.flinkful.rest.base.v1.messages.webmonitor.ClusterOverviewWithVersion;
import feign.Headers;
import feign.RequestLine;

public interface ClusterClientV1 {

    @RequestLine("GET v1/overview")
    @Headers("Content-Type: application/json")
    ClusterOverviewWithVersion overview();

    @RequestLine("DELETE v1/cluster")
    @Headers("Content-Type: application/json")
    void shutdownCluster();
}
