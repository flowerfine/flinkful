package cn.sliew.flinkful.rest.base.v1.messages.webmonitor;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class JobsOverview {

    @JsonProperty("jobs-running")
    private int numJobsRunningOrPending;

    @JsonProperty("jobs-finished")
    private int numJobsFinished;

    @JsonProperty("jobs-cancelled")
    private int numJobsCancelled;

    @JsonProperty("jobs-failed")
    private int numJobsFailed;
}
