package cn.sliew.flinkful.rest.http;

import cn.sliew.milky.common.exception.Rethrower;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import okio.Buffer;
import okio.BufferedSource;
import org.jetbrains.annotations.NotNull;

import java.io.EOFException;
import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.Charset;
import java.nio.charset.UnsupportedCharsetException;
import java.util.stream.Collectors;

@Slf4j
public class LogInterceptor implements Interceptor {

    private static final Charset UTF8 = Charset.forName("UTF-8");

    @NotNull
    @Override
    public Response intercept(@NotNull Chain chain) {
        Request request = chain.request();
        logRequest(chain.request());
        try {
            Response response = chain.proceed(request);
            logResponse(response);
            return response;
        } catch (Exception e) {
            log.error("OkHttpClient 处理请求异常", e);
            Rethrower.throwAs(e);
            return null;
        }
    }

    private void logRequest(Request request) {
        String method = request.method();
        String url = request.url().toString();
        String requestBody = getRequestBody(request.body());
        String headers = request.headers().toMultimap().entrySet()
                .stream()
                .map(entry -> "            " + entry.getKey() + ": " + String.join(";", entry.getValue()))
                .collect(Collectors.joining("\n"));
        if (log.isInfoEnabled()) {
            log.info("{} {} requestBody: {}, headers: \n{}", method, url, requestBody, headers);
        }
    }

    private void logResponse(Response response) throws IOException {
        int code = response.code();
        String message = response.message();
        String responseBody = "";
        if (response.body() != null) {
            responseBody = getResponseBody(response.body());
        }
        String headers = response.headers().toMultimap().entrySet()
                .stream()
                .map(entry -> "            " + entry.getKey() + ": " + String.join(";", entry.getValue()))
                .collect(Collectors.joining("\n"));
        long duration = response.receivedResponseAtMillis() - response.sentRequestAtMillis();
        if (log.isInfoEnabled()) {
            log.info("请求耗时: {}ms, code: {}, message: {}, responseBody: {}, headers: \n{}",
                    duration, code, message, responseBody, headers);
        }

    }

    private String getRequestBody(RequestBody requestBody) {
        if (requestBody == null) {
            return "";
        }
        if (requestBody instanceof MultipartBody) {
            return "";
        }
        Buffer buffer = new Buffer();
        try {
            requestBody.writeTo(buffer);
            return URLDecoder.decode(buffer.readUtf8(), "utf-8");
        } catch (IOException e) {
            log.error("读取 OKHttpClient 请求体异常!", e);
            return "";
        }
    }

    private String getResponseBody(ResponseBody responseBody) throws IOException {
        BufferedSource source = responseBody.source();
        source.request(Long.MAX_VALUE);
        Buffer buffer = source.buffer();

        Charset charset = UTF8;
        MediaType contentType = responseBody.contentType();
        if (contentType != null) {
            try {
                charset = contentType.charset(UTF8);
            } catch (UnsupportedCharsetException e) {
                log.error("获取 OKHttpClient 响应编码异常!", e);
            }
        }

        if (!isPlaintext(buffer)) {
            return null;
        }

        if (responseBody.contentLength() != 0) {
            String result = buffer.clone().readString(charset);
            return result;
        }
        return null;
    }

    private boolean isPlaintext(Buffer buffer) {
        try {
            Buffer prefix = new Buffer();
            long byteCount = buffer.size() < 64 ? buffer.size() : 64;
            buffer.copyTo(prefix, 0, byteCount);
            for (int i = 0; i < 16; i++) {
                if (prefix.exhausted()) {
                    break;
                }
                int codePoint = prefix.readUtf8CodePoint();
                if (Character.isISOControl(codePoint) && !Character.isWhitespace(codePoint)) {
                    return false;
                }
            }
            return true;
        } catch (EOFException e) {
            return false;
        }
    }

}
