package cn.sliew.flinkful.sql.gateway.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.flink.runtime.rest.RestClient;
import org.apache.flink.runtime.rest.messages.EmptyMessageParameters;
import org.apache.flink.runtime.rest.messages.EmptyRequestBody;
import org.apache.flink.table.gateway.rest.header.util.GetApiVersionHeaders;
import org.apache.flink.table.gateway.rest.header.util.GetInfoHeaders;
import org.apache.flink.table.gateway.rest.message.util.GetApiVersionResponseBody;
import org.apache.flink.table.gateway.rest.message.util.GetInfoResponseBody;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping
@Api(tags = "util 接口")
public class UtilController {

    @Autowired
    private RestClient client;

    @GetMapping("api_versions")
    @ApiOperation("Get the current available versions for the Rest Endpoint. " +
            "The client can choose one of the return version as the protocol for later communicate.")
    public CompletableFuture<GetApiVersionResponseBody> getApiVersion() throws IOException {
        return client.sendRequest("localhost", 8083,
                GetApiVersionHeaders.getInstance(),
                EmptyMessageParameters.getInstance(),
                EmptyRequestBody.getInstance());
    }

    @GetMapping("info")
    @ApiOperation("Get meta data for this cluster.")
    public CompletableFuture<GetInfoResponseBody> getInfo() throws IOException {
        return client.sendRequest("localhost", 8083,
                GetInfoHeaders.getInstance(),
                EmptyMessageParameters.getInstance(),
                EmptyRequestBody.getInstance());
    }


}
