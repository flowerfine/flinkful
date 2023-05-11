package cn.sliew.flinkful.rest.base.v1.messages.webmonitor;

public enum RestoreMode {

    CLAIM("Flink will take ownership of the given snapshot. It will clean the snapshot once it is subsumed by newer ones."),
    NO_CLAIM("Flink will not claim ownership of the snapshot files. However it will make sure it does not depend on any artefacts from the restored snapshot. In order to do that, Flink will take the first checkpoint as a full one, which means it might reupload/duplicate files that are part of the restored checkpoint."),
    LEGACY("This is the mode in which Flink worked so far. It will not claim ownership of the snapshot and will not delete the files. However, it can directly depend on the existence of the files of the restored checkpoint. It might not be safe to delete checkpoints that were restored in legacy mode ");

    private final String description;

    RestoreMode(String description) {
        this.description = description;
    }
}
