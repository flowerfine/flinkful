package cn.sliew.flinkful.common.enums;

import lombok.Getter;
import cn.sliew.flinkful.shade.org.apache.flink.client.deployment.executors.RemoteExecutor;
import cn.sliew.flinkful.shade.org.apache.flink.configuration.Configuration;
import cn.sliew.flinkful.shade.org.apache.flink.configuration.DeploymentOptions;
import cn.sliew.flinkful.shade.org.apache.flink.core.execution.PipelineExecutorFactory;
import cn.sliew.flinkful.shade.org.apache.flink.kubernetes.configuration.KubernetesDeploymentTarget;
import cn.sliew.flinkful.shade.org.apache.flink.yarn.configuration.YarnDeploymentTarget;

@Getter
public enum DeploymentTarget {

    STANDALONE_APPLICATION(0, ResourceProvider.STANDALONE, DeploymentMode.APPLICATION, RemoteExecutor.NAME),
    STANDALONE_SESSION(1, ResourceProvider.STANDALONE, DeploymentMode.SESSION, RemoteExecutor.NAME),

    /**
     * 以 kubernetes 作为 resource provider 时需要在提交机器上提前设置好 kubeconfig 文件，供 flink 连接 kubernetes 集群。
     */
    NATIVE_KUBERNETES_APPLICATION(2, ResourceProvider.NATIVE_KUBERNETES, DeploymentMode.APPLICATION, KubernetesDeploymentTarget.APPLICATION.getName()),
    NATIVE_KUBERNETES_SESSION(3, ResourceProvider.NATIVE_KUBERNETES, DeploymentMode.SESSION, KubernetesDeploymentTarget.SESSION.getName()),

    /**
     * 以 YARN 作为 resource provider 时需要在提交机器上提前设置好 hadoop 配置，供 flink 连接 YARN 集群。
     * 配置 hadoop 配置可以通过 $HADOOP_HOME 变量提供。
     */
    YARN_APPLICATION(4, ResourceProvider.YARN, DeploymentMode.APPLICATION, YarnDeploymentTarget.APPLICATION.getName()),
    YARN_PER_JOB(5, ResourceProvider.YARN, DeploymentMode.PER_JOB, YarnDeploymentTarget.PER_JOB.getName()),
    YARN_SESSION(6, ResourceProvider.YARN, DeploymentMode.SESSION, YarnDeploymentTarget.SESSION.getName()),
    ;

    private int code;
    private ResourceProvider resourceProvider;
    private DeploymentMode deploymentMode;

    /**
     * @see PipelineExecutorFactory#getName()
     */
    private String name;

    DeploymentTarget(int code, ResourceProvider resourceProvider, DeploymentMode deploymentMode, String name) {
        this.resourceProvider = resourceProvider;
        this.deploymentMode = deploymentMode;
        this.name = name;
    }

    public void apply(Configuration configuration) {
        configuration.set(DeploymentOptions.TARGET, getName());
    }
}
