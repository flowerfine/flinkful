package cn.sliew.flinkful.kubernetes.sql.gateway;

public interface SqlGatewayFactory {

    SqlGateway create(KubernetesSqlGateway kubernetes);
}
