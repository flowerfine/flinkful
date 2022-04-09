package cn.sliew.flinkful.rest.base;

import org.apache.flink.runtime.rest.messages.EmptyResponseBody;
import org.apache.flink.runtime.rest.messages.JobPlanInfo;
import org.apache.flink.runtime.webmonitor.handlers.*;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;

public interface JarClient {

    /**
     * Returns a list of all jars previously uploaded via '/jars/upload'.
     */
    CompletableFuture<JarListInfo> jars() throws IOException;

    /**
     * Uploads a jar to the cluster.
     * The jar must be sent as multi-part data.
     * Make sure that the "Content-Type" header is set to "application/x-java-archive", as some http libraries do not add the header by default.
     * Using 'curl' you can upload a jar via 'curl -X POST -H "Expect:" -F "jarfile=@path/to/flink-job.jar" http://hostname:port/jars/upload'.
     */
    CompletableFuture<JarUploadResponseBody> uploadJar(String filePath) throws IOException;

    /**
     * Deletes a jar previously uploaded via '/jars/upload'.
     *
     * @param jarId String value that identifies a jar. When uploading the jar a path is returned, where the filename is the ID. This value is equivalent to the `id` field in the list of uploaded jars (/jars).
     */
    CompletableFuture<EmptyResponseBody> deleteJar(String jarId) throws IOException;

    /**
     * Returns the dataflow plan of a job contained in a jar previously uploaded via '/jars/upload'.
     * Program arguments can be passed both via the JSON request (recommended) or query parameters.
     *
     * @param jarId       String value that identifies a jar. When uploading the jar a path is returned, where the filename is the ID. This value is equivalent to the `id` field in the list of uploaded jars (/jars).
     * @param requestBody
     */
    CompletableFuture<JobPlanInfo> jarPlan(String jarId, JarPlanRequestBody requestBody) throws IOException;

    /**
     * Submits a job by running a jar previously uploaded via '/jars/upload'.
     * Program arguments can be passed both via the JSON request (recommended) or query parameters.
     *
     * @param jarId       String value that identifies a jar. When uploading the jar a path is returned, where the filename is the ID. This value is equivalent to the `id` field in the list of uploaded jars (/jars).
     * @param requestBody
     */
    CompletableFuture<JarRunResponseBody> jarRun(String jarId, JarRunRequestBody requestBody) throws IOException;

}
