package cn.sliew.flinkful.kubernetes.operator.entity.logging;

import lombok.Data;

@Data
public class LoggerPair {

    private String logger;
    private String level;
}
