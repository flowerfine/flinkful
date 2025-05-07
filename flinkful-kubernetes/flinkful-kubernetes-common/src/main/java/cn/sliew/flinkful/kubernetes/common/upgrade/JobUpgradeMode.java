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
