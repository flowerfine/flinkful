package cn.sliew.flinkful.cli.base.session;

import cn.sliew.flinkful.common.enums.DeploymentTarget;

public enum SessionFactory {
    ;

    public static SessionCommand buildSessionCommand(DeploymentTarget target) {
        switch (target) {
            case STANDALONE_SESSION:
                throw new UnsupportedOperationException();
            case NATIVE_KUBERNETES_SESSION:
                return new KubernetesSessionCreateCommand();
            default:
                throw new UnsupportedOperationException();
        }
    }
}
