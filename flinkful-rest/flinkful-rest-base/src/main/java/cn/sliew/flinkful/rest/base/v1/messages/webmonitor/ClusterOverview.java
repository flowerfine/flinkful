package cn.sliew.flinkful.rest.base.v1.messages.webmonitor;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class ClusterOverview extends JobsOverview {

    @JsonProperty("taskmanagers")
    private int numTaskManagersConnected;

    @JsonProperty("slots-total")
    private int numSlotsTotal;

    @JsonProperty("slots-available")
    private int numSlotsAvailable;

    @JsonProperty("taskmanagers-blocked")
    private int numTaskManagersBlocked;

    @JsonProperty("slots-free-and-blocked")
    private int numSlotsFreeAndBlocked;
}
