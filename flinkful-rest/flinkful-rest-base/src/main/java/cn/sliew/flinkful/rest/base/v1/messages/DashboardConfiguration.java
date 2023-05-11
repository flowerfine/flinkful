package cn.sliew.flinkful.rest.base.v1.messages;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class DashboardConfiguration {

    @JsonProperty("refresh-interval")
    private long refreshInterval;

    @JsonProperty("timezone-name")
    private String timeZoneName;

    @JsonProperty("timezone-offset")
    private int timeZoneOffset;

    @JsonProperty("flink-version")
    private String flinkVersion;

    @JsonProperty("flink-revision")
    private String flinkRevision;

    @JsonProperty("features")
    private Features features;

    @Data
    public static final class Features {

        @JsonProperty("web-submit")
        private boolean webSubmitEnabled;

        @JsonProperty("web-cancel")
        private boolean webCancelEnabled;

        @JsonProperty("web-history")
        private boolean isHistoryServer;
    }
}