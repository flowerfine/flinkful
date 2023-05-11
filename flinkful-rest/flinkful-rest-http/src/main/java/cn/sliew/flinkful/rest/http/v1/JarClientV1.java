package cn.sliew.flinkful.rest.http.v1;

import cn.sliew.flinkful.rest.base.v1.messages.JobPlanInfo;
import cn.sliew.flinkful.rest.base.v1.messages.webmonitor.*;
import feign.Headers;
import feign.Param;
import feign.RequestLine;

import java.io.File;

public interface JarClientV1 {

    @RequestLine("GET v1/jars")
    @Headers("Content-Type: application/json")
    JarListInfo jars();

    @RequestLine("POST v1/jars/upload")
    @Headers("Content-Type: application/java-archive")
    JarUpload upload(@Param("jarfile") File jarfile);

    @RequestLine("DELETE v1/jars/{jarId}")
    @Headers("Content-Type: application/json")
    void delete(@Param("jarId") String jarId);

    @RequestLine("POST v1/jars/{jarId}/plan")
    @Headers("Content-Type: application/json")
    JobPlanInfo plan(@Param("jarId") String jarId, JarPlanRequestBody requestBody);

    @RequestLine("GET v1/jars/{jarId}/run")
    @Headers("Content-Type: application/json")
    JarRunResponseBody run(@Param("jarId") String jarId, JarRunRequestBody requestBody);

}
