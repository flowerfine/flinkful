package cn.sliew.flinkful.rest.base;

public interface RestClient {

    ClusterClient cluster();

    DataSetClient dataSet();

    JarClient jar();

    JobClient job();

    JobVerticeClient jobVertice();

    JobManagerClient jobManager();

    TaskManagerClient taskManager();

    SavepointClient savepoint();

    DashboardClient dashboard();
}
