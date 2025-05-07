package cn.sliew.flinkful.kubernetes.operator.parameters;

import cn.sliew.carp.framework.kubernetes.definition.ResourceParameter;
import lombok.Data;
import lombok.experimental.SuperBuilder;

import java.util.UUID;

@Data
@SuperBuilder
public abstract class FlinkResourceParameter implements ResourceParameter {

    private final UUID id;
    private final String name;
    private final String namespace;
    private final String internalNamespace;
}
