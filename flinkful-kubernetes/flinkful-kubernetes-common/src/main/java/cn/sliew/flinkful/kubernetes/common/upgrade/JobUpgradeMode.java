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
package cn.sliew.flinkful.kubernetes.common.upgrade;

import cn.sliew.carp.framework.common.jackson.polymorphic.Polymorphic;
import cn.sliew.carp.framework.common.jackson.polymorphic.PolymorphicResolver;
import cn.sliew.flinkful.kubernetes.common.artifact.JarArtifact;
import cn.sliew.flinkful.kubernetes.common.dict.operator.UpgradeMode;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonTypeIdResolver;

@JsonTypeIdResolver(JobUpgradeMode.JobUpgradeModeResolver.class)
@JsonIgnoreProperties(ignoreUnknown = true)
public interface JobUpgradeMode extends Polymorphic<UpgradeMode> {

    default boolean isAllowNonRestoredState() {
        return false;
    }

    final class JobUpgradeModeResolver extends PolymorphicResolver<UpgradeMode> {

        public JobUpgradeModeResolver() {
            bindDefault(JarArtifact.class);
            bind(UpgradeMode.STATELESS, StatelessUpgradeMode.class);
            bind(UpgradeMode.LAST_STATE, LastStateUpgradeMode.class);
            bind(UpgradeMode.SAVEPOINT, SavepointUpgradeMode.class);
        }

        @Override
        protected String typeFromSubtype(Object obj) {
            return subTypes.inverse().get(obj.getClass()).getValue();
        }

        @Override
        protected Class<?> subTypeFromType(String id) {
            Class<?> subType = subTypes.get(UpgradeMode.of(id));
            return subType != null ? subType : defaultClass;
        }
    }
}
