[![Gihub Actions](https://github.com/flowerfine/flinkful/actions/workflows/ci.yml/badge.svg?branch=main)](https://github.com/flowerfine/flinkful/actions) [![Last commit](https://img.shields.io/github/last-commit/flowerfine/flinkful.svg)](https://github.com/flowerfine/flinkful) [![GitHub Tag](https://img.shields.io/github/v/tag/flowerfine/flinkful)](https://github.com/flowerfine/flinkful/tags) [![Maven Central](https://img.shields.io/maven-central/v/cn.sliew/flinkful)](https://maven-badges.herokuapp.com/maven-central/cn.sliew/flinkful) [![License](https://img.shields.io/github/license/flowerfine/flinkful.svg)](http://www.apache.org/licenses/LICENSE-2.0.html)

# Flinkful

## Why Flinkful?

Flink is a popular stateful compute engine and it has provided a few ways for management such as cli, http, python and scala REPLs,  which would only satisfy some purposes and people have to choose one or  all of them for needs.

I have done some exciting work which developed generic Java Flink client supporting submit jobs、access cluster  and jobs status, and more features to be adding. Through it, I believed people can concern more on how to manage and maintain flink infrastructure prevent by the absence of Java client.

Flinkful aims to help people build their own data and computation platform by Flink and would never shade Flink. We don't make gap between people with Flink but try to bridge them totally, that's why named `Flinkful`.

Flinkful makes best effort for releasing Flink source code internal potentials and preventing introducing new concept. If you dive into Flinkful, We believe the clean and Flink likely source code will confuse user are reading Flink official repository code.

Hope user like and appreciate our work.

## Quick Start

Flinkful helps people open the door to Flink management and provides `flinkful-examples` module which demonstrates creating session cluster, submitting job and access jobs and cluster status.

* `flinkful-common-examples`. helper module contains flink environment and job utility. 
* `flinkful-cli-examples`. modules contains how to create session cluster and submit job.
* `flinkful-rest-examples`. modules contains how to access jobs and cluster status.
* `flinkful-rest-examples`. modules contains how to access 1.16 flink sql gateway.

Get started with Flinkful firstly, add Flinkful as a dependency in your Java project. 

If you're using Maven, that looks like this:

```xml
<repositories>
    <repository>
        <id>sonatype-nexus</id>
        <name>Sonatype Nexus Snapshots</name>
        <url>https://s01.oss.sonatype.org/content/repositories/snapshots/</url>
    </repository>
</repositories>

<dependencies>
    <dependency>
        <groupId>cn.sliew</groupId>
        <artifactId>flinkful-cli-base</artifactId>
        <version>${flinkful.version}</version>
    </dependency>
    <dependency>
        <groupId>cn.sliew</groupId>
        <artifactId>flinkful-cli-descriptor</artifactId>
        <version>${flinkful.version}</version>
    </dependency>
    
    <dependency>
        <groupId>cn.sliew</groupId>
        <artifactId>flinkful-rest-base</artifactId>
        <version>${flinkful.version}</version>
    </dependency>
    <dependency>
        <groupId>cn.sliew</groupId>
        <artifactId>flinkful-rest-client</artifactId>
        <version>${flinkful.version}</version>
    </dependency>
</dependencies>
```

## How Flinkful?

As mentioned [here](https://nightlies.apache.org/flink/flink-docs-release-1.14/docs/deployment/overview/), Flink consists of `JobManager`, `TaskManager` and `client`. After `JobManager` bootstraped, people can access Flink cluster with runtime-webui by http, In fact, most Flink cluster and job status can be obtained by http.

Flink supports 3 deployment mode:

* Application
* Per-Job
* Session

Application and Per-Job deployment mode will create a new Flink cluster through resource providers such as YARN or Kubernetes, so Flink http API is limited on job submission scene for http API just working well  with `JobManager`, you can't require more when `JobManager` not exists.

Except that, there is `WebOptions#SUBMIT_ENABLE` option indicating whether jobs can be uploaded and run from the web-frontend.

So job submission and Flink cluster and job status access can vary significantly, Flinkful distinguish them on two module: `flinkful-cli` and `flinkful-rest`, the former for submiting jobs and latter for cluster and job status accessment.

### Job Submission

#### `ClusterDescriptor`

![image-20220412220000689](README.assets/image-20220412220000689.png)

For complex deployment ways, it is also a challenage for Flink on how to design cluster client API and `ClusterDescriptor` would be the answer.

Through `ClusterDescriptor`, Flink can create deployment mode cluster on different resource providers:

* `#deploySessionCluster` creates Session cluster.
* `#deployApplication` creates Application cluster.
* `#deployJobCluster` creates Per-Job Cluster.

For different resource providers, Flink provides corresponding implementation:

* `StandaloneClusterDescriptor` supports `RemoteExecutor#NAME`.
* `YarnClusterDescriptor` supports `YarnDeploymentTarget#APPLICATION`, `YarnDeploymentTarget#PER_JOB`, `YarnDeploymentTarget#SESSION`.
* `KubernetesClusterDescriptor` supports `KubernetesDeploymentTarget#APPLICATION`, `KubernetesDeploymentTarget#SESSION`.

With the helpment of `ClusterDescriptor`, Flinkful provides `flinkful-cli-descriptor` for submiting jobs by `ClusterClient`.

#### `CliFrontend`

`$FLINK_HOME/bin/flink` is Flink job submission entrypoint and `CliFrontend` is cli interface core class, we can also be inspired by it.

cut cli parameter parse and command route, we find a clean job sumission implementation follow:

```java
public class FrontendCliClient implements CliClient {

    private final ClusterClientServiceLoader clusterClientServiceLoader = new DefaultClusterClientServiceLoader();
    private final ApplicationDeployer deployer = new ApplicationClusterDeployer(clusterClientServiceLoader);

    private final PipelineExecutorServiceLoader pipelineExecutorServiceLoader = new DefaultExecutorServiceLoader();

    /**
     * @see CliFrontend#run(String[])
     */
    @Override
    public void submit(DeploymentTarget deploymentTarget, Configuration configuration, PackageJarJob job) throws Exception {
        deploymentTarget.apply(configuration);
        try (PackagedProgram program = FlinkUtil.buildProgram(configuration, job)) {
            ClientUtils.executeProgram(pipelineExecutorServiceLoader, configuration, program, false, false);
        }
    }

    /**
     * @see CliFrontend#runApplication(String[])
     */
    @Override
    public void submitApplication(DeploymentTarget deploymentTarget, Configuration configuration, PackageJarJob job) throws Exception {
        deploymentTarget.apply(configuration);
        ApplicationConfiguration applicationConfiguration = new ApplicationConfiguration(job.getProgramArgs(), job.getEntryPointClass());
        deployer.run(configuration, applicationConfiguration);
    }
}
```

User can add `flinkful-cli-frontend` to their project to obtain this clean code.

### Access cluster and job status

#### http client

Flink has a monitoring API  for runtime web-ui, here is [link](https://nightlies.apache.org/flink/flink-docs-release-1.14/docs/ops/rest_api/).

There are a few excellent http clients for executing http request, Flinkful implements it by `OkHttp` on `flinkful-rest-http`.

If you are tired of http API query and path parameters, also [`openfeign`](https://github.com/spring-cloud/spring-cloud-openfeign) and [`retrofit`](https://github.com/square/retrofit) is a good choice for Flink http API implemention by yourself.

#### `RestClient`

Thankfully, Flink provides `RestClient` for runtime web-ui access internally and Flinkful help people access cluster and job status by `RestClient` easily.

User can explore Flinkful what to do on `flinkful-rest-client` module.

### Session Cluster

```java
public interface ClusterDescriptor<T> extends AutoCloseable {

    /**
     * Triggers deployment of a cluster.
     *
     * @param clusterSpecification Cluster specification defining the cluster to deploy
     * @return Client for the cluster
     * @throws ClusterDeploymentException if the cluster could not be deployed
     */
    ClusterClientProvider<T> deploySessionCluster(ClusterSpecification clusterSpecification) throws ClusterDeploymentException;
}
```

`ClusterDescriptor` defines the method deploying session cluster and Flink implements YARN and Native Kubernetes ways, Flinkful's `SessionClient` supports all of them.

```java
public class SessionClient {

    public static ClusterClient create(DeploymentTarget deploymentTarget, Configuration configuration) throws Exception {
        switch (deploymentTarget) {
            case NATIVE_KUBERNETES_SESSION:
            case YARN_SESSION:
            case STANDALONE_SESSION:
                SessionCommand command = SessionFactory.buildSessionCommand(deploymentTarget);
                deploymentTarget.apply(configuration);
                return command.create(deploymentTarget, configuration);
            default:
                throw new UnsupportedOperationException();
        }
    }
}

public enum SessionFactory {
    ;

    public static SessionCommand buildSessionCommand(DeploymentTarget target) {
        switch (target) {
            case STANDALONE_SESSION:
                throw new UnsupportedOperationException();
            case YARN_SESSION:
                return new YarnSessionCreateCommand();
            case NATIVE_KUBERNETES_SESSION:
                return new KubernetesSessionCreateCommand();
            default:
                throw new UnsupportedOperationException();
        }
    }
}
```

### Flink Sql Gateway

Flink 1.16 releases sql gateway to support multiple clients from the remote to execute SQL in concurrency. It provides an easy way to submit the Flink Job, look up the metadata, and analyze the data online.

Flinkful has added flink sql gateway client to `flinkful-rest` modules and developer can use them directly.

## Flink vs Flinkful

| java | Flink  | Flinkful             | branch    | service    |
| ---- | ------ | -------------------- | --------- | ---------- |
| 1.8  | 1.13.6 | 1.13.6_1_8-SNAPSHOT  | 1.13/main | deprecated |
| 11   | 1.14.4 | 1.14.2_1_11-SNAPSHOT | 1.14/main | deprecated |
| 11   | 1.15.1 | 1.15.1_1_17-SNAPSHOT | 1.15/main | deprecated |
| 11   | 1.16.0 | 1.16.0_1_11-SNAPSHOT | 1.16/main | latest     |
| 11   | 1.17.0 | 1.17.0_1_11-SNAPSHOT | main      | latest     |

## Next Flinkful

* QueryableState client.
* [Flink Kubernetes Operator](https://github.com/apache/flink-kubernetes-operator). done

## License

Flinkful is licenced under the Apache License Version 2.0, link is [here](https://www.apache.org/licenses/LICENSE-2.0.txt).