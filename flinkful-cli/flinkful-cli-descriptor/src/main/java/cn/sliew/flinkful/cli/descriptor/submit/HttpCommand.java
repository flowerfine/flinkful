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
package cn.sliew.flinkful.cli.descriptor.submit;

import cn.sliew.flinkful.cli.base.submit.PackageJarJob;
import cn.sliew.flinkful.cli.descriptor.protocol.JarRunRequest;
import cn.sliew.flinkful.cli.descriptor.protocol.JarRunResponse;
import cn.sliew.flinkful.cli.descriptor.protocol.JarUploadResponse;
import cn.sliew.milky.common.util.JacksonUtil;
import org.apache.flink.api.common.JobID;
import org.apache.flink.client.program.ClusterClient;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.configuration.RestOptions;
import org.apache.hc.client5.http.entity.mime.MultipartEntityBuilder;
import org.apache.hc.client5.http.fluent.Request;
import org.apache.hc.core5.http.ContentType;
import org.apache.hc.core5.http.io.entity.StringEntity;
import org.apache.hc.core5.util.Timeout;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.stream.Collectors;

public class HttpCommand implements SubmitCommand {

    @Override
    public ClusterClient submit(Path flinkHome, Configuration configuration, PackageJarJob job) throws Exception {
        String webInterfaceURL = configuration.get(RestOptions.ADDRESS);
        JarUploadResponse jarUploadResponse = uploadJar(webInterfaceURL, new File(job.getJarFilePath()));
        String jarId = jarUploadResponse.getFilename().substring(jarUploadResponse.getFilename().lastIndexOf("/") + 1);
        run(webInterfaceURL, jarId, job);
        return null;
    }

    private JarUploadResponse uploadJar(String webInterfaceURL, File jarFile) throws IOException {
        String response = Request.post(webInterfaceURL + "/jars/upload")
                .connectTimeout(Timeout.ofSeconds(3L))
                .responseTimeout(Timeout.ofSeconds(3L))
                .body(
                        MultipartEntityBuilder.create()
                                .addBinaryBody("jarfile", jarFile, ContentType.create("application/java-archive"), "SocketWindowWordCount.jar")
                                .build()
                ).execute().returnContent().asString(StandardCharsets.UTF_8);
        return JacksonUtil.parseJsonString(response, JarUploadResponse.class);
    }

    private JobID run(String webInterfaceURL, String jarId, PackageJarJob job) throws IOException {
        JarRunRequest jarRunRequest = new JarRunRequest();
        jarRunRequest.setEntryClass(job.getEntryPointClass());
        jarRunRequest.setProgramArgs(Arrays.asList(job.getProgramArgs()).stream().collect(Collectors.joining("  ")));

        String response = Request.post(webInterfaceURL + "/jars/" + jarId + "/run")
                .connectTimeout(Timeout.ofSeconds(3L))
                .responseTimeout(Timeout.ofSeconds(60))
                .body(new StringEntity(JacksonUtil.toJsonString(jarRunRequest)))
                .execute().returnContent().asString(StandardCharsets.UTF_8);
        String jobID = JacksonUtil.parseJsonString(response, JarRunResponse.class).getJobID();
        return JobID.fromHexString(jobID);
    }
}
