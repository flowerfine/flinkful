package cn.sliew.flinkful.cli.descriptor.submit;

import cn.sliew.flinkful.common.enums.DeploymentTarget;

public enum CommandFactory {
    ;

    public static SubmitCommand buildSubmitCommand(DeploymentTarget target) {
        switch (target) {
            case STANDALONE_SESSION:
                return new RestClusterClientCommand();
            case STANDALONE_APPLICATION:
                throw new UnsupportedOperationException();
            case YARN_SESSION:
                return new YarnSessionSubmitCommand();
            case YARN_APPLICATION:
                return new YarnApplicationCommand();
            case YARN_PER_JOB:
                return new YarnPerJobCommand();
            case NATIVE_KUBERNETES_SESSION:
                return new KubernetesSessionSubmitCommand();
            case NATIVE_KUBERNETES_APPLICATION:
                return new KubernetesApplicationCommand();
            default:
                throw new UnsupportedOperationException();
        }
    }

}
