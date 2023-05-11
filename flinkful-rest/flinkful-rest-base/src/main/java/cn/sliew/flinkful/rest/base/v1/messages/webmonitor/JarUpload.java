package cn.sliew.flinkful.rest.base.v1.messages.webmonitor;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class JarUpload {

    @JsonProperty("status")
    private UploadStatus status;

    @JsonProperty("filename")
    private String filename;

    enum UploadStatus {
        success;
    }
}
