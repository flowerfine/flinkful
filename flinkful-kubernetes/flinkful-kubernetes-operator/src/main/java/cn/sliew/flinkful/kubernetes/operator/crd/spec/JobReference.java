package cn.sliew.flinkful.kubernetes.operator.crd.spec;


import cn.sliew.flinkful.kubernetes.operator.crd.AbstractFlinkResource;
import cn.sliew.flinkful.kubernetes.operator.crd.CrdConstants;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Flink resource reference that can be a FlinkDeployment or FlinkSessionJob.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class JobReference {

    /**
     * Kind of the Flink resource, FlinkDeployment or FlinkSessionJob.
     */
    private JobKind kind;

    /**
     * Name of the Flink resource.
     */
    private String name;

    public static JobReference fromFlinkResource(AbstractFlinkResource<?, ?> flinkResource) {
        var result = new JobReference();
        result.setName(flinkResource.getMetadata().getName());

        if (flinkResource instanceof FlinkDeployment) {
            result.setKind(JobKind.FLINK_DEPLOYMENT);
        } else if (flinkResource instanceof FlinkSessionJob) {
            result.setKind(JobKind.FLINK_SESSION_JOB);
        }

        return result;
    }

    public String toString() {
        String kindString = kind.name();
        if (kind == JobKind.FLINK_DEPLOYMENT) {
            kindString = CrdConstants.KIND_FLINK_DEPLOYMENT;
        } else if (kind == JobKind.FLINK_SESSION_JOB) {
            kindString = CrdConstants.KIND_SESSION_JOB;
        }
        return String.format("%s (%s)", name, kindString);
    }
}
