package cn.sliew.flinkful.rest.client.controller;

import cn.sliew.flinkful.rest.base.SqlGateWayClient;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.flink.table.gateway.rest.message.session.OpenSessionRequestBody;
import org.apache.flink.table.gateway.rest.message.session.OpenSessionResponseBody;
import org.apache.flink.table.gateway.rest.message.statement.ExecuteStatementRequestBody;
import org.apache.flink.table.gateway.rest.message.statement.ExecuteStatementResponseBody;
import org.apache.flink.table.gateway.rest.message.statement.FetchResultsResponseBody;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/flink/sql/gateway")
@Api(value = "/gateway", tags = "SQL gateway接口")
public class SqlGateWayContorller {

    @Autowired
    private SqlGateWayClient sqlGateWayClient;

    @PostMapping
    @ApiOperation("open session")
    public CompletableFuture<OpenSessionResponseBody> openSession() throws IOException {
        OpenSessionRequestBody requestBody = new OpenSessionRequestBody("", null);
        return sqlGateWayClient.sqlProcess().openSession(requestBody);
    }


    @PostMapping("{sessionHandle}")
    @ApiOperation("open session")
    public CompletableFuture<ExecuteStatementResponseBody> executeStatement(
            @PathVariable String sessionHandle,
            @RequestBody ExecuteStatementRequestBody requestBody)
            throws IOException {
        return sqlGateWayClient.sqlProcess().executeStatement(sessionHandle, requestBody);
    }


    @PostMapping("/statement/res")
    @ApiOperation("open session")
    public CompletableFuture<FetchResultsResponseBody> executeStatement(String sessionHandle,
                                                                        String operationHandle, String token)
            throws IOException {
        return sqlGateWayClient.sqlProcess()
                .getStatementResult(sessionHandle, operationHandle, token);
    }

}
