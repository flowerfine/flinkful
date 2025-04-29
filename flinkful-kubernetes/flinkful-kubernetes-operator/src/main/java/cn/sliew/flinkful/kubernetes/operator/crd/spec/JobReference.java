package cn.sliew.flinkful.kubernetes.operator.crd.spec;

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
}
