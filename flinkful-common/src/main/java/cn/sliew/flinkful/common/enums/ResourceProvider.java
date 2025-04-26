package cn.sliew.flinkful.common.enums;

import lombok.Getter;

@Getter
public enum ResourceProvider {

    STANDALONE(0, "Standalone"),
    NATIVE_KUBERNETES(1, "Native Kubernetes");

    private int code;
    private String name;

    ResourceProvider(int code, String name) {
        this.code = code;
        this.name = name;
    }
}
