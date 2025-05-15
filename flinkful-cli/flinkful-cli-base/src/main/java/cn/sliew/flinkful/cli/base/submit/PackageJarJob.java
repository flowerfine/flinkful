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
package cn.sliew.flinkful.cli.base.submit;

import cn.sliew.milky.common.primitives.Strings;
import lombok.Getter;
import lombok.Setter;
import org.apache.flink.client.cli.CliFrontendParser;
import org.apache.flink.client.cli.ProgramOptions;
import org.apache.flink.runtime.jobgraph.SavepointConfigOptions;
import org.apache.flink.runtime.jobgraph.SavepointRestoreSettings;

import java.net.URL;
import java.util.Collections;
import java.util.List;

/**
 * @see ProgramOptions
 */
@Getter
@Setter
public class PackageJarJob {

    /**
     * Flink program JAR file.
     */
    private String jarFilePath;

    /**
     * Class with the program entry point ("main()" method).
     * Only needed if the JAR file does not specify the class in its manifest.
     * refer JarManifestParser#findEntryClass
     */
    private String entryPointClass;

    /**
     * Adds a URL to each user code classloader  on all nodes in the cluster.
     * The paths must specify a protocol (e.g. file://) and be accessible on all nodes (e.g. by means of a NFS share).
     * The protocol must be supported by the {@link java.net.URLClassLoader}.
     */
    private List<URL> classpaths = Collections.emptyList();

    /**
     * Program arguments.
     */
    private String[] programArgs = Strings.EMPTY_ARRAY;

    /**
     * The parallelism with which to run the program.
     * Optional flag to override the default value specified in the configuration.
     */
    private int parallelism;

    /**
     * If present, runs the job in detached mode
     */
    private boolean detachedMode;

    /**
     * @see CliFrontendParser#SAVEPOINT_PATH_OPTION
     * @see CliFrontendParser#SAVEPOINT_ALLOW_NON_RESTORED_OPTION
     * @see CliFrontendParser#SAVEPOINT_RESTORE_MODE
     * @see SavepointConfigOptions#RESTORE_MODE
     */
    private SavepointRestoreSettings savepointSettings = SavepointRestoreSettings.none();
}
