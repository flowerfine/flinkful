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
package cn.sliew.flinkful.kubernetes.operator.crd.spec;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Resource spec.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Resource {

    /**
     * Amount of CPU allocated to the pod.
     */
    private Double cpu;

    /**
     * Amount of memory allocated to the pod. Example: 1024m, 1g
     */
    private String memory;

    /**
     * Amount of ephemeral storage allocated to the pod. Example: 1024m, 2G
     */
    private String ephemeralStorage;
}
