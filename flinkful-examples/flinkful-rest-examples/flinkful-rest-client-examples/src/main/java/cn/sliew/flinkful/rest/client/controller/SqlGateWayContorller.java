package cn.sliew.flinkful.rest.client.controller;

import cn.sliew.flinkful.rest.base.SqlGateWayClient;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.io.IOException;
import java.util.concurrent.CompletableFuture;
import org.apache.flink.runtime.rest.messages.ConversionException;
import org.apache.flink.table.gateway.rest.message.session.OpenSessionResponseBody;
import org.apache.flink.table.gateway.rest.message.statement.ExecuteStatementRequestBody;
import org.apache.flink.table.gateway.rest.message.statement.ExecuteStatementResponseBody;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/flink/sql/gateway")
@Api(value = "/gateway", tags = "SQL gateway接口")
public class SqlGateWayContorller {

    @Autowired
    private SqlGateWayClient sqlGateWayClient;

    @PostMapping
    @ApiOperation("open session")
    public CompletableFuture<OpenSessionResponseBody> openSession() throws IOException {
        return sqlGateWayClient.sqlProcess().openSession();
    }


    @PostMapping("{sessionHandle}")
    @ApiOperation("open session")
    public CompletableFuture<ExecuteStatementResponseBody> executeStatement(
        @PathVariable String sessionHandle,
        @RequestBody ExecuteStatementRequestBody requestBody)
        throws IOException, ConversionException {
        return sqlGateWayClient.sqlProcess().executeStatement(sessionHandle, requestBody);
    }

}
