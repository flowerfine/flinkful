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
