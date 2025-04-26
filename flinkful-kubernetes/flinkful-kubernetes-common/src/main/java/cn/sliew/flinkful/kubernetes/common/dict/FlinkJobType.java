package cn.sliew.flinkful.kubernetes.common.dict;

import cn.sliew.carp.framework.common.dict.DictInstance;
import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

@Getter
@RequiredArgsConstructor
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum FlinkJobType implements DictInstance {

    UNKNOWN("-1", "Unknown"),
    JAR("0", "Jar"),
    SQL("1", "SQL"),
    SEATUNNEL("2", "SeaTunnel"),
    FLINK_CDC("3", "Flink CDC"),
    ;

    @JsonCreator
    public static FlinkJobType of(String value) {
        return Arrays.stream(values())
                .filter(instance -> instance.getValue().equals(value))
                .findAny().orElseThrow(() -> new EnumConstantNotPresentException(FlinkJobType.class, value));
    }

    @EnumValue
    private final String value;
    private final String label;
}
