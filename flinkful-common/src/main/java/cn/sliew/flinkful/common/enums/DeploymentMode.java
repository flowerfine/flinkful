package cn.sliew.flinkful.common.enums;

import lombok.Getter;

@Getter
public enum DeploymentMode {

    APPLICATION(0, "Application"),
    PER_JOB(1, "Per-Job"),
    SESSION(2, "Session");

    private int code;
    private String name;

    DeploymentMode(int code, String name) {
        this.code = code;
        this.name = name;
    }
}
