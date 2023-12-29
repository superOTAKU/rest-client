package org.sky.restclient;

public interface HttpRequestBuilder<B extends HttpRequestBuilder<B>> extends HttpResponseFetcher {

    B header(String name, String value);

    B query(String name, String value);

    B body(Object body);

}
