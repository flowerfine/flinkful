package cn.sliew.flinkful.rest.base.v1.messages.webmonitor;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class ClusterOverviewWithVersion extends ClusterOverview {

    @JsonProperty("flink-version")
    private final String version;

    @JsonProperty("flink-commit")
    private final String commitId;
}
