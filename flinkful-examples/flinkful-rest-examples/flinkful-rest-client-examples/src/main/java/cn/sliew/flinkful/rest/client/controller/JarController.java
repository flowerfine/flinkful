package cn.sliew.flinkful.rest.client.controller;

import cn.sliew.flinkful.rest.base.v1.client.JarClient;
import cn.sliew.flinkful.rest.base.v1.client.RestClient;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.flink.runtime.rest.messages.EmptyResponseBody;
import org.apache.flink.runtime.rest.messages.JobPlanInfo;
import org.apache.flink.runtime.webmonitor.handlers.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/jars")
@Api(value = "/jars", tags = "jars接口")
public class JarController {

    @Autowired
    private RestClient restClient;

    /**
     * https://nightlies.apache.org/flink/flink-docs-release-1.15/docs/ops/rest_api/#jars
     */
    @GetMapping
    @ApiOperation("上传 jars 列表")
    public CompletableFuture<JarListInfo> jars() throws IOException {
        JarClient jarClient = restClient.jar();
        return jarClient.jars();
    }

    /**
     * https://nightlies.apache.org/flink/flink-docs-release-1.15/docs/ops/rest_api/#jars-upload
     */
    @PostMapping("upload")
    @ApiOperation("上传 jar")
    public CompletableFuture<JarUploadResponseBody> upload(@RequestParam("filePath") String filePath) throws IOException {
        JarClient jarClient = restClient.jar();
        return jarClient.uploadJar(filePath);
    }

    /**
     * https://nightlies.apache.org/flink/flink-docs-release-1.15/docs/ops/rest_api/#jars-jarid
     */
    @DeleteMapping("{jarId}")
    @ApiOperation("删除 jar")
    public CompletableFuture<EmptyResponseBody> delete(@PathVariable("jarId") String jarId) throws IOException {
        JarClient jarClient = restClient.jar();
        return jarClient.deleteJar(jarId);
    }

    /**
     * https://nightlies.apache.org/flink/flink-docs-release-1.15/docs/ops/rest_api/#jars-jarid-plan
     */
    @GetMapping("{jarId}/plan")
    @ApiOperation("查看 jar 的 dataflow plan")
    public CompletableFuture<JobPlanInfo> jarPlan(@PathVariable("jarId") String jarId, JarPlanRequestBody requestBody) throws IOException {
        JarClient jarClient = restClient.jar();
        return jarClient.jarPlan(jarId, requestBody);
    }

    /**
     * https://nightlies.apache.org/flink/flink-docs-release-1.15/docs/ops/rest_api/#jars-jarid-plan-1
     */
    @PostMapping("{jarId}/run")
    @ApiOperation("提交 jar 到集群中运行")
    public CompletableFuture<JarRunResponseBody> jarRun(@PathVariable("jarId") String jarId, @RequestBody JarRunRequestBody requestBody) throws IOException {
        JarClient jarClient = restClient.jar();
        return jarClient.jarRun(jarId, requestBody);
    }
}
