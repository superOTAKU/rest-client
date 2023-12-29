package org.sky.restclient;

import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.OkHttpClient;

import java.time.Duration;

public interface RestClientConfig {

    String getBaseUrl();

    RestClientConfig setBaseUrl(String baseUrl);

    Duration getConnectTimeout();

    RestClientConfig setConnectTimeout(Duration timeout);

    Duration getReadTimeout();

    RestClientConfig setReadTimeout(Duration timeout);

    Duration getWriteTimeout();

    RestClientConfig setWriteTimeout(Duration timeout);

    ObjectMapper getObjectMapper();

    RestClientConfig setObjectMapper(ObjectMapper objectMapper);

    OkHttpClient getOkHttpClient();

    RestClientConfig setOkHttpClient(OkHttpClient client);

    RestClient client();

}
