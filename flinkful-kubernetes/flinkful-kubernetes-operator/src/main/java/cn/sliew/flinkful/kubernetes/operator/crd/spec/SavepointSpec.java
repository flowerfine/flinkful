package cn.sliew.flinkful.kubernetes.operator.crd.spec;

import cn.sliew.flinkful.kubernetes.operator.crd.status.SavepointFormatType;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Spec for savepoint state snapshots.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class SavepointSpec {
    /**
     * Optional path for the savepoint.
     */
    private String path;

    /**
     * Savepoint format to use.
     */
    private SavepointFormatType formatType = SavepointFormatType.CANONICAL;

    /**
     * Dispose the savepoints upon CR deletion.
     */
    private Boolean disposeOnDelete = true;

    /**
     * Indicates that the savepoint already exists on the given path. The Operator will not trigger
     * any new savepoints, just update the status of the resource as a completed snapshot.
     */
    private Boolean alreadyExists = false;
}
