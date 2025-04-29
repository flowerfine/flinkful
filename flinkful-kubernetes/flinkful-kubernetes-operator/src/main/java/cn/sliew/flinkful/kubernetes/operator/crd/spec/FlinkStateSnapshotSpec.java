package cn.sliew.flinkful.kubernetes.operator.crd.spec;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.apache.commons.lang3.builder.DiffResult;
import org.apache.commons.lang3.builder.Diffable;
import org.apache.commons.lang3.builder.ReflectionDiffBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * Spec that describes a FlinkStateSnapshot.
 */
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class FlinkStateSnapshotSpec implements Diffable<FlinkStateSnapshotSpec> {

    /**
     * Source to take a snapshot of. Not required if it's a savepoint and alreadyExists is true.
     */
    private JobReference jobReference;

    /**
     * Spec in case of savepoint.
     */
    private SavepointSpec savepoint = null;

    /**
     * Spec in case of checkpoint.
     */
    private CheckpointSpec checkpoint = null;

    /**
     * Maximum number of retries before the snapshot is considered as failed. Set to -1 for
     * unlimited or 0 for no retries.
     */
    private int backoffLimit = -1;

    public boolean isSavepoint() {
        return savepoint != null;
    }

    public boolean isCheckpoint() {
        return checkpoint != null;
    }

    @Override
    public DiffResult diff(FlinkStateSnapshotSpec right) {
        ReflectionDiffBuilder builder = new ReflectionDiffBuilder(this, right, ToStringStyle.DEFAULT_STYLE);
        return builder.build();
    }
}
