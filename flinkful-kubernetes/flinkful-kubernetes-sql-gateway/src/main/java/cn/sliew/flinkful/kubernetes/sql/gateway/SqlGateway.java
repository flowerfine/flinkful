package cn.sliew.flinkful.kubernetes.sql.gateway;

public interface SqlGateway {

    SqlGatewayResource getResource();

    void start();

    boolean isRunning();

    void stopAsync();

    boolean isStopped();
}
