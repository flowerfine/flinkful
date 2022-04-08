package cn.sliew.flinkful.common.enums;

import lombok.Getter;

@Getter
public enum JobType {

    JAR(0, "jar"),
    SQL(1, "SQL"),
    ;

    private int code;
    private String desc;

    JobType(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }
}
