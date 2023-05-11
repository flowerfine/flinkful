package cn.sliew.flinkful.rest.base.v1.messages.dataset;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class ClusterDataSetList {

    @JsonProperty("dataSets")
    private List<ClusterDataSetEntry> dataSets;
}
