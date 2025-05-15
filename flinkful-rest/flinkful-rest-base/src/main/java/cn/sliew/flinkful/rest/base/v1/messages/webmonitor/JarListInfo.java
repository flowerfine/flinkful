/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
