package cn.sliew.flinkful.kubernetes.operator.definitions.handler;

import cn.sliew.carp.framework.common.dict.k8s.CarpK8sImagePullPolicy;
import cn.sliew.flinkful.kubernetes.common.dict.FlinkImage;
import cn.sliew.flinkful.kubernetes.common.dict.FlinkJobType;
import cn.sliew.flinkful.kubernetes.common.dict.operator.FlinkOperatorFlinkVersion;
import cn.sliew.flinkful.kubernetes.operator.crd.spec.*;
import cn.sliew.flinkful.kubernetes.operator.definitions.handler.flinkconfiguration.FileSystemStepDecorator;
import cn.sliew.flinkful.kubernetes.operator.definitions.handler.flinkconfiguration.FlinkConfigurationStepDecorator;
import cn.sliew.flinkful.kubernetes.operator.definitions.handler.flinkconfiguration.FlinkStateStorageStepDecorator;
import cn.sliew.flinkful.kubernetes.operator.definitions.handler.flinkconfiguration.SessionClusterServiceStepDecorator;
import cn.sliew.flinkful.kubernetes.operator.definitions.handler.jobmanagerspec.FileFetcherInitContainerStepDecorator;
import cn.sliew.flinkful.kubernetes.operator.definitions.handler.jobmanagerspec.JobManagerSpecStepDecorator;
import cn.sliew.flinkful.kubernetes.operator.definitions.handler.podtemplate.FileFetcherMainContainerStepDecorator;
import cn.sliew.flinkful.kubernetes.operator.definitions.handler.podtemplate.FlinkFileSystemPluginStepDecorator;
import cn.sliew.flinkful.kubernetes.operator.definitions.handler.podtemplate.FlinkMainContainerStepDecorator;
import cn.sliew.flinkful.kubernetes.operator.definitions.handler.podtemplate.PodTemplateStepDecorator;
import cn.sliew.flinkful.kubernetes.operator.parameters.SessionClusterParameters;
import cn.sliew.flinkful.kubernetes.operator.util.FlinkConfigurations;
import com.google.common.base.Joiner;
import io.fabric8.kubernetes.api.model.HasMetadata;
import io.fabric8.kubernetes.api.model.Pod;
import io.fabric8.kubernetes.api.model.PodBuilder;
import org.apache.commons.lang3.EnumUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class DefaultFlinkSessionClusterSpecProvider implements FlinkSessionClusterSpecProvider {

    private final SessionClusterParameters parameters;

    private final List<FlinkConfigurationStepDecorator> flinkConfigurationStepDecorators;
    private final List<PodTemplateStepDecorator> podTemplateStepDecorators;
    private final List<JobManagerSpecStepDecorator> jobManagerSpecStepDecorators;

    private FlinkSessionClusterSpec spec;
    private List<HasMetadata> additionalResources = new ArrayList<>();

    public DefaultFlinkSessionClusterSpecProvider(SessionClusterParameters parameters) {
        this.parameters = parameters;
        this.flinkConfigurationStepDecorators = Arrays.asList(
                new FileSystemStepDecorator(parameters.getProperties()),
                new FlinkStateStorageStepDecorator(parameters.getProperties(), parameters.getId()),
                new SessionClusterServiceStepDecorator()
        );
        this.podTemplateStepDecorators = Arrays.asList(
                new FlinkMainContainerStepDecorator(parameters.getLabels()),
                new FlinkFileSystemPluginStepDecorator(parameters.getFlinkVersion(), parameters.getProperties()),
                new FileFetcherMainContainerStepDecorator()
        );
        this.jobManagerSpecStepDecorators = Arrays.asList(
                new FileFetcherInitContainerStepDecorator(parameters.getProperties(), parameters.getFileFetcherParams())
        );

        buildSpec();
    }

    private void buildSpec() {
        spec = FlinkSessionClusterSpec.builder()
                .imagePullPolicy(getImagePullPolicy().getValue())
                .image(getImage())
                .flinkVersion(getFlinkVersion())
                .serviceAccount(getServiceAccount())
                .jobManager(getJobManagerSpec())
                .taskManager(getTaskManagerSpec())
                .logConfiguration(getLogConfiguration())
                .flinkConfiguration(getFlinkConfiguration())
                .podTemplate(getPodTemplate())
                .mode(KubernetesDeploymentMode.NATIVE)
                .build();
    }

    @Override
    public FlinkSessionClusterSpec getSpec() {
        return spec;
    }

    @Override
    public List<HasMetadata> getAdditionalResources() {
        return additionalResources;
    }

    private CarpK8sImagePullPolicy getImagePullPolicy() {
        return CarpK8sImagePullPolicy.IF_NOT_PRESENT;
    }

    private String getImage() {
        FlinkImage flinkImage = FlinkImage.ofFlinkVersion(FlinkJobType.JAR, parameters.getFlinkVersion());
        return flinkImage.getValue();
    }

    private OperatorFlinkVersion getFlinkVersion() {
        FlinkOperatorFlinkVersion flinkOperatorFlinkVersion = FlinkOperatorFlinkVersion.of(parameters.getFlinkVersion());
        return EnumUtils.getEnum(OperatorFlinkVersion.class, flinkOperatorFlinkVersion.getValue());
    }

    private String getServiceAccount() {
        return "flink";
    }

    private JobManagerSpec getJobManagerSpec() {
        JobManagerSpec spec = JobManagerSpec.builder()
                .resource(new Resource(1.0, "1G", null))
                .replicas(1)
                .build();
        for (JobManagerSpecStepDecorator decorator : jobManagerSpecStepDecorators) {
            spec = decorator.decorate(spec);
            additionalResources.addAll(decorator.buildRelatedResources());
        }
        return spec;
    }

    private TaskManagerSpec getTaskManagerSpec() {
        return TaskManagerSpec.builder()
                .resource(new Resource(1.0, "1G", null))
                .replicas(1)
                .build();
    }

    private Map<String, String> getLogConfiguration() {
        Map<String, String> loggers = Map.of("cn.sliew", "DEBUG");
        String logConfig = Joiner.on("\n").withKeyValueSeparator(" = ").join(loggers);
        return Map.of("log4j-console.properties", logConfig);
    }

    private Map<String, String> getFlinkConfiguration() {
        Map<String, String> flinkConfiguration = FlinkConfigurations.createFlinkConfiguration();
        for (FlinkConfigurationStepDecorator decorator : flinkConfigurationStepDecorators) {
            flinkConfiguration = decorator.decorate(flinkConfiguration);
            additionalResources.addAll(decorator.buildRelatedResources());
        }
        return flinkConfiguration;
    }

    private Pod getPodTemplate() {
        Pod podTemplate = new PodBuilder().build();
        for (PodTemplateStepDecorator decorator : podTemplateStepDecorators) {
            podTemplate = decorator.decorate(podTemplate);
            additionalResources.addAll(decorator.buildRelatedResources());
        }
        return podTemplate;
    }
}
