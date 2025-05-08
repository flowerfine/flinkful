package cn.sliew.flinkful.kubernetes.sql.gateway;

public interface KubernetesSqlGatewayFactory {

    KubernetesSqlGateway create(SqlGatewayResource resource);
}
