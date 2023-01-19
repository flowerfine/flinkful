package cn.sliew.flinkful.kubernetes.operator;

import cn.sliew.flinkful.kubernetes.operator.configurer.ObjectMetaConfigurer;
import cn.sliew.flinkful.kubernetes.operator.configurer.SpecConfigurer;
import cn.sliew.milky.dsl.Customizer;
import org.apache.flink.kubernetes.operator.api.FlinkDeployment;

public enum Util {
    ;

    public static FlinkDeployment build() throws Exception {
        FlinkDeploymentBuilder config = new FlinkDeploymentBuilder();
        config
                .apiVersion(Customizer.withDefaults())
                .kind(Customizer.withDefaults())
                .metadata(Util::metadata)
                .spec(Util::spec);

        return config.getOrBuild();
    }

    private static void metadata(ObjectMetaConfigurer objectMetaConfigurer) {
        objectMetaConfigurer.name("basic");
    }

    private static void spec(SpecConfigurer specConfigurer) {
        specConfigurer
                .ingress(Util::ingress)
                .jobManager(Util::jobManager)
                .taskManager(Util::taskManager)
                .podTemplate(Util::podTemplate)
                .job(Util::job)
                .flinkConfiguration("taskmanager.numberOfTaskSlots", "2");
    }

    private static void ingress(SpecConfigurer.IngressSpecConfig config) {
        config.template("{{name}}.{{namespace}}.flink.k8s.io");
    }

    private static void jobManager(SpecConfigurer.JobManagerSpecConfig config) {

    }

    private static void taskManager(SpecConfigurer.TaskManagerSpecConfig config) {

    }

    private static void podTemplate(SpecConfigurer.PodTemplateSpecConfig config) {

    }

    private static void job(SpecConfigurer.JobSpecConfig config) {
        config.jarURI("local:///opt/flink/examples/streaming/StateMachineExample.jar")
                .parallelism(2);
    }
}
