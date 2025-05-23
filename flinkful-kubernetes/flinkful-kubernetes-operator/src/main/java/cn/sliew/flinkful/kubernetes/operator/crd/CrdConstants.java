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
package cn.sliew.flinkful.kubernetes.operator.crd;

/**
 * Constants used by the CRD.
 */
public class CrdConstants {

    public static final String API_GROUP = "flink.apache.org";
    public static final String API_VERSION = "v1beta1";
    public static final String KIND_SESSION_JOB = "FlinkSessionJob";
    public static final String KIND_FLINK_DEPLOYMENT = "FlinkDeployment";
    public static final String KIND_FLINK_STATE_SNAPSHOT = "FlinkStateSnapshot";

    public static final String LABEL_TARGET_SESSION = "target.session";

    public static final String EPHEMERAL_STORAGE = "ephemeral-storage";

    public static final String LABEL_SNAPSHOT_TYPE = "snapshot.type";
    public static final String LABEL_SNAPSHOT_TRIGGER_TYPE = "snapshot.trigger-type";
    public static final String LABEL_SNAPSHOT_STATE = "snapshot.state";
    public static final String LABEL_SNAPSHOT_JOB_REFERENCE_KIND = "job-reference.kind";
    public static final String LABEL_SNAPSHOT_JOB_REFERENCE_NAME = "job-reference.name";
}
