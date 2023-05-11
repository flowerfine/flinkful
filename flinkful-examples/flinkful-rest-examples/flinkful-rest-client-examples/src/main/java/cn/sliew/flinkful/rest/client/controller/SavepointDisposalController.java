package cn.sliew.flinkful.rest.client.controller;

import cn.sliew.flinkful.rest.base.v1.client.RestClient;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.flink.runtime.rest.handler.async.AsynchronousOperationInfo;
import org.apache.flink.runtime.rest.handler.async.AsynchronousOperationResult;
import org.apache.flink.runtime.rest.handler.async.TriggerResponse;
import org.apache.flink.runtime.rest.messages.job.savepoints.SavepointDisposalRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/savepoint-disposal")
@Api(value = "/savepoint-disposal", tags = "savepoint销毁接口")
public class SavepointDisposalController {

    @Autowired
    private RestClient restClient;

    /**
     * https://nightlies.apache.org/flink/flink-docs-release-1.13/docs/ops/rest_api/#savepoint-disposal
     */
    @PostMapping
    @ApiOperation("异步销毁 savepoint")
    public CompletableFuture<TriggerResponse> dispose(@RequestBody SavepointDisposalRequest request) throws IOException {
        return restClient.savepoint().savepointDisposal(request);
    }

    /**
     * https://nightlies.apache.org/flink/flink-docs-release-1.13/docs/ops/rest_api/#savepoint-disposal-triggerid
     */
    @GetMapping("{triggerId}")
    @ApiOperation("异步销毁 savepoint 结果")
    public CompletableFuture<AsynchronousOperationResult<AsynchronousOperationInfo>> disposal(@PathVariable("triggerId") String triggerId) throws IOException {
        return restClient.savepoint().savepointDisposalResult(triggerId);
    }
}
