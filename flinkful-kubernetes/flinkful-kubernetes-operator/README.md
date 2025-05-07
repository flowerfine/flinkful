# Flink Kubernetes Operator

## 配置详情

### Artifact

不同种类任务适配

| artifact  | Deployment | SessionJob | 说明 |
| --------- | ---------- | ---------- | ---- |
| sql       | todo       | todo       |      |
| jar       | todo       | todo       |      |
| seatunnel | todo       | todo       |      |
| flink-cdc | todo       | todo       |      |
| python    | todo       | todo       |      |

`jarURI` 支持类型：

* local。默认在 flink 镜像本地，无需处理。
  * 只有 Deployment 支持，SessionJob 不支持。因为 SessionCluster 先启动，Operator 调用 SessionCluster 的上传 jar 接口，传入任务 jar 并启动任务
* http。通过 http 加载。
* FileSystem。通过 flink FileSystem 加载。

artifact 参考：

* [python-example.yaml](https://github.com/apache/flink-kubernetes-operator/blob/main/examples/flink-python-example/python-example.yaml)
* [sql-example.yaml](https://github.com/apache/flink-kubernetes-operator/blob/main/examples/flink-sql-runner-example/sql-example.yaml)
* deployment。[basic.yaml](https://github.com/apache/flink-kubernetes-operator/blob/main/examples/basic.yaml)
* sessioin。用的是 http，不支持 local
  * [basic-session-deployment-and-job.yaml](https://github.com/apache/flink-kubernetes-operator/blob/main/examples/basic-session-deployment-and-job.yaml)
  * [basic-session-job-only.yaml](https://github1s.com/apache/flink-kubernetes-operator/blob/main/examples/basic-session-job-only.yaml)

### 资源

| 组件        | cpu  | memory |
| ----------- | ---- | ------ |
| JobManager  | todo | todo   |
| TaskManager | todo | todo   |
| PodTemplate | todo | todo   |

### PodTemplate

pod template 优先级: 全局 -> job-manager, task-manger

参考：

* [pod-template.yaml](https://github.com/apache/flink-kubernetes-operator/blob/main/examples/pod-template.yaml)

flink-main-container

#### InitContainer

初始化加载动作

#### Sidecar

日志采集，监控采集

#### ConfigMap

configmap

#### SecretValue

secretvalue

### Flink 配置

checkpoint、savepoint、ha

重启

定时 savepoint

### Ingress

* [basic-ingress.yaml](https://github.com/apache/flink-kubernetes-operator/blob/main/examples/basic-ingress.yaml)
* [Ingress](https://nightlies.apache.org/flink/flink-kubernetes-operator-docs-release-1.11/docs/operations/ingress/)

### 日志

* [custom-logging.yaml](https://github.com/apache/flink-kubernetes-operator/blob/main/examples/custom-logging.yaml)

### 启动参数

