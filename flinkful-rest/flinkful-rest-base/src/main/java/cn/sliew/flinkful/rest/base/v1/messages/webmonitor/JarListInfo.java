package cn.sliew.flinkful.rest.base.v1.messages.webmonitor;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.annotation.Nullable;
import java.util.List;

@Data
public class JarListInfo {

    @JsonProperty("address")
    private String address;

    @JsonProperty("files")
    public List<JarFileInfo> jarFileList;

    @Data
    public static class JarFileInfo {

        @JsonProperty("id")
        public String id;

        @JsonProperty("name")
        public String name;

        @JsonProperty("uploaded")
        private long uploaded;

        @JsonProperty("entry")
        private List<JarEntryInfo> jarEntryList;
    }

    @Data
    public static class JarEntryInfo {

        @JsonProperty("name")
        private String name;

        @Nullable
        @JsonProperty("description")
        private String description;
    }
}
