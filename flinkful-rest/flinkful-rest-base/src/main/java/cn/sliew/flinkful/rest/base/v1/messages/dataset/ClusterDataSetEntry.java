package cn.sliew.flinkful.rest.base.v1.messages.dataset;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class ClusterDataSetEntry {

    @JsonProperty("id")
    private String dataSetId;

    @JsonProperty("isComplete")
    private boolean isComplete;
}
