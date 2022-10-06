package cn.sliew.flinkful.kubernetes.operator;

import cn.sliew.flinkful.kubernetes.operator.configurer.ObjectMetaConfigurer;
import cn.sliew.flinkful.kubernetes.operator.configurer.SpecConfigurer;
import cn.sliew.milky.dsl.Customizer;
import org.apache.flink.kubernetes.operator.crd.FlinkDeployment;

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
        objectMetaConfigurer.name("Basic");
    }

    private static void spec(SpecConfigurer specConfigurer) {
        specConfigurer
                .ingress(Util::ingress)
                .jobManager(Util::jobManager)
                .taskManager(Util::taskManager)
                .podTemplate(Util::podTemplate)
                .job(Util::job);
    }

    private static void ingress(SpecConfigurer.IngressSpecConfig config) {

    }

    private static void jobManager(SpecConfigurer.JobManagerSpecConfig config) {

    }

    private static void taskManager(SpecConfigurer.TaskManagerSpecConfig config) {

    }

    private static void podTemplate(SpecConfigurer.PodTemplateSpecConfig config) {

    }

    private static void job(SpecConfigurer.JobSpecConfig config) {

    }
}
