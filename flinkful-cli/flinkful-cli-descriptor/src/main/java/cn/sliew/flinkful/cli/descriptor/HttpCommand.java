package cn.sliew.flinkful.cli.descriptor;

import cn.sliew.flinkful.cli.base.PackageJarJob;
import cn.sliew.flinkful.cli.descriptor.protocol.JarRunRequest;
import cn.sliew.flinkful.cli.descriptor.protocol.JarRunResponse;
import cn.sliew.flinkful.cli.descriptor.protocol.JarUploadResponse;
import cn.sliew.milky.common.util.JacksonUtil;
import org.apache.flink.api.common.JobID;
import org.apache.flink.configuration.Configuration;
import org.apache.hc.client5.http.entity.mime.MultipartEntityBuilder;
import org.apache.hc.client5.http.fluent.Request;
import org.apache.hc.core5.http.ContentType;
import org.apache.hc.core5.http.io.entity.StringEntity;
import org.apache.hc.core5.util.Timeout;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.stream.Collectors;

public class HttpCommand implements Command {

    @Override
    public void submit(Configuration configuration, PackageJarJob job) throws Exception {
        String webInterfaceURL = "http://localhost:8081";
        JarUploadResponse jarUploadResponse = uploadJar(webInterfaceURL, new File(job.getJarFilePath()));
        String jarId = jarUploadResponse.getFilename().substring(jarUploadResponse.getFilename().lastIndexOf("/") + 1);
        JobID jobID = run(webInterfaceURL, jarId, job);
        System.out.println(jobID);
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
