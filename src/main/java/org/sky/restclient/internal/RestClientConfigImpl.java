package org.sky.restclient.internal;

import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.OkHttpClient;
import org.sky.restclient.RestClient;
import org.sky.restclient.RestClientConfig;

import java.time.Duration;
import java.util.Objects;

public class RestClientConfigImpl implements RestClientConfig {
    private static final Duration DEFAULT_TIMEOUT = Duration.ofSeconds(30L);
    private String baseUrl;
    private ObjectMapper objectMapper = new ObjectMapper();
    private OkHttpClient okHttpClient = new OkHttpClient().newBuilder()
            .connectTimeout(DEFAULT_TIMEOUT)
            .readTimeout(DEFAULT_TIMEOUT)
            .writeTimeout(DEFAULT_TIMEOUT).build();
    private final RestClient restClient;

    public RestClientConfigImpl(RestClient restClient) {
        this.restClient = restClient;
    }

    @Override
    public RestClientConfig setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
        return this;
    }

    @Override
    public String getBaseUrl() {
        return baseUrl;
    }

    @Override
    public RestClientConfig setObjectMapper(ObjectMapper objectMapper) {
        this.objectMapper = Objects.requireNonNull(objectMapper, "object mapper can't be null");
        return this;
    }

    @Override
    public ObjectMapper getObjectMapper() {
        return objectMapper;
    }

    @Override
    public RestClientConfig setOkHttpClient(OkHttpClient okHttpClient) {
        this.okHttpClient = Objects.requireNonNull(okHttpClient, "okHttpClient can't be null");
        return this;
    }

    @Override
    public RestClient client() {
        return restClient;
    }

    @Override
    public OkHttpClient getOkHttpClient() {
        return okHttpClient;
    }

    @Override
    public Duration getConnectTimeout() {
        return Duration.ofMillis(okHttpClient.readTimeoutMillis());
    }

    @Override
    public RestClientConfig setConnectTimeout(Duration timeout) {
        okHttpClient = okHttpClient.newBuilder().connectTimeout(timeout).build();
        return this;
    }

    @Override
    public Duration getReadTimeout() {
        return Duration.ofMillis(okHttpClient.readTimeoutMillis());
    }

    @Override
    public RestClientConfig setReadTimeout(Duration timeout) {
        okHttpClient = okHttpClient.newBuilder().connectTimeout(timeout).build();
        return this;
    }

    @Override
    public Duration getWriteTimeout() {
        return Duration.ofMillis(okHttpClient.writeTimeoutMillis());
    }

    @Override
    public RestClientConfig setWriteTimeout(Duration timeout) {
        okHttpClient = okHttpClient.newBuilder().writeTimeout(timeout).build();
        return this;
    }


}
