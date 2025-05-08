package cn.sliew.flinkful.kubernetes.sql.gateway;

public interface SqlGatewayResourceFactory {

    SqlGatewayResource create(SqlGatewayResourceDefinition resourceDefinition);
}
