package cn.sliew.flinkful.rest.client.controller;

import cn.sliew.flinkful.rest.base.JobClient;
import cn.sliew.flinkful.rest.base.RestClient;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.flink.runtime.rest.messages.job.JobDetailsInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/job")
@Api(value = "/job", tags = "")
public class JobController {

    @Autowired
    private RestClient restClient;

    @GetMapping("jobDetail")
    @ApiOperation("任务详情")
    public CompletableFuture<JobDetailsInfo> jobDetail() throws IOException {
        JobClient jobClient = restClient.job();
        return jobClient.jobDetail("b9f4f2411cd946dbf8c8923012aa4010");
    }
}
