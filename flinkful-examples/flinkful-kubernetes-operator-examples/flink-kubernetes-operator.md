# flink-kubernetes-operator

## submit

需要额外带着一个唯一名称，后续操作都需要这个唯一名称

### application

需要带着 job

### session

不带 job 信息

## suspend

https://nightlies.apache.org/flink/flink-kubernetes-operator-docs-release-1.2/docs/custom-resource/job-management/#running-suspending-and-deleting-applications

## cancel



## savepoint



创建 savepoint

从 savepoint 启动

自动周期创建 savepoint

```yaml
flinkConfiguration:
    ...
    kubernetes.operator.periodic.savepoint.interval: 6h
```

savepoint 自动清理

```yaml
kubernetes.operator.savepoint.history.max.age: 24 h
kubernetes.operator.savepoint.history.max.count: 5
```



## restart



## log



## plugin



## upgrade



## session



## application

