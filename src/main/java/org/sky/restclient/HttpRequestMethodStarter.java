package org.sky.restclient;

public interface HttpRequestMethodStarter {

    HttpRequestBuilder<?> get(String url, String ... urlVariables);

    HttpRequestBuilder<?> post(String url, String ... urlVariables);

    HttpRequestBuilder<?> put(String url, String ... urlVariables);

    HttpRequestBuilder<?> delete(String url, String ... urlVariables);
}
