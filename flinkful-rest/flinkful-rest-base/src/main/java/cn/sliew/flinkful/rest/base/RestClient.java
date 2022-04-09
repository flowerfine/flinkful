package cn.sliew.flinkful.rest.base;

public interface RestClient {

    ClusterClient cluster();

    DataSetClient dataSet();

    JarClient jar();

    JobClient job();

    JobManagerClient jobManager();

    TaskManagerClient taskManager();

    SavepointClient savepoint();

    DashboardClient dashboard();
}
