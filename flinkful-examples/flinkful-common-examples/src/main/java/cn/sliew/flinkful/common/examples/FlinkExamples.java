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
package cn.sliew.flinkful.common.examples;

import org.apache.flink.configuration.Configuration;
import org.apache.flink.configuration.GlobalConfiguration;

public enum FlinkExamples {
    ;

    private static final String FLINK_HOME = System.getenv("FLINK_HOME");

    private static final String FLINK_CONF_DIR = FLINK_HOME + "/conf";
    private static final String FLINK_PLUGINS_DIR = FLINK_HOME + "/plugins";
    private static final String FLINK_LIB_DIR = FLINK_HOME + "/lib";
    private static final String FLINK_EXAMPLES_DIR = FLINK_HOME + "/examples";
    public static final String FLINK_DIST_JAR = FLINK_HOME + "/lib/flink-dist-1.15.1.jar";

    public static final String EXAMPLE_JAR = FLINK_EXAMPLES_DIR + "/streaming/TopSpeedWindowing.jar";
    public static final String EXAMPLE_JAR_URL = "file://" + FLINK_EXAMPLES_DIR + "/streaming/TopSpeedWindowing.jar";
    public static final String EXAMPLE_ENTRY_CLASS = "org.apache.flink.streaming.examples.windowing.TopSpeedWindowing";

    public static Configuration loadConfiguration() {
        return GlobalConfiguration.loadConfiguration(FLINK_CONF_DIR, new Configuration());
    }

}
