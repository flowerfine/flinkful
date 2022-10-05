package cn.sliew.flinkful.cli.descriptor.submit;

import cn.sliew.flinkful.cli.base.submit.PackageJarJob;
import cn.sliew.flinkful.cli.descriptor.protocol.JarRunRequest;
import cn.sliew.flinkful.cli.descriptor.protocol.JarRunResponse;
import cn.sliew.flinkful.cli.descriptor.protocol.JarUploadResponse;
import cn.sliew.milky.common.util.JacksonUtil;
import cn.sliew.flinkful.shade.org.apache.flink.api.common.JobID;
import cn.sliew.flinkful.shade.org.apache.flink.client.program.ClusterClient;
import cn.sliew.flinkful.shade.org.apache.flink.configuration.Configuration;
import cn.sliew.flinkful.shade.org.apache.flink.configuration.RestOptions;
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
