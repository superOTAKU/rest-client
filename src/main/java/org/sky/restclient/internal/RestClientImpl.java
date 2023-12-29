package org.sky.restclient.internal;

import org.sky.restclient.HttpRequestBuilder;
import org.sky.restclient.RestClient;
import org.sky.restclient.RestClientConfig;

public class RestClientImpl implements RestClient {
    private final RestClientConfig config = new RestClientConfigImpl(this);

    @Override
    public HttpRequestBuilder<?> get(String url, String... urlVariables) {
        return new HttpRequestBuilderImpl(this, "GET", url, urlVariables);
    }

    @Override
    public HttpRequestBuilder<?> post(String url, String... urlVariables) {
        return new HttpRequestBuilderImpl(this, "POST", url, urlVariables);
    }

    @Override
    public HttpRequestBuilder<?> put(String url, String... urlVariables) {
        return new HttpRequestBuilderImpl(this, "PUT", url, urlVariables);
    }

    @Override
    public HttpRequestBuilder<?> delete(String url, String... urlVariables) {
        return new HttpRequestBuilderImpl(this, "DELETE", url, urlVariables);
    }

    @Override
    public RestClientConfig config() {
        return config;
    }
}
