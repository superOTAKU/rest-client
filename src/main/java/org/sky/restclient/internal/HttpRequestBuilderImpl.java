package org.sky.restclient.internal;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import okhttp3.*;
import org.sky.restclient.HttpRequestBuilder;
import org.sky.restclient.HttpResponse;
import org.sky.restclient.RestClient;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

public class HttpRequestBuilderImpl implements HttpRequestBuilder<HttpRequestBuilderImpl> {
    private final RestClient restClient;
    private Request.Builder requestBuilder;
    private HttpUrl.Builder urlBuilder;
    private final String method;

    public HttpRequestBuilderImpl(RestClient restClient, String method, String url, String ... urlVariables) {
        this.restClient = restClient;
        this.method = method.toUpperCase();
        initUrlBuilder(url, urlVariables);
        requestBuilder = new Request.Builder().url(urlBuilder.build());
    }

    private void initUrlBuilder(String url, String ... urlVariables) {
        String urlWithVars = url;
        for (var urlVariable : urlVariables) {
            urlWithVars = urlWithVars.replaceFirst("\\{}", urlVariable);
        }
        if (!url.startsWith("http://") && !url.startsWith("https://") && restClient.config().getBaseUrl() != null) {
            urlBuilder = Objects.requireNonNull(HttpUrl.parse(restClient.config().getBaseUrl()))
                    .newBuilder().addPathSegments(urlWithVars);
        } else {
            urlBuilder = Objects.requireNonNull(HttpUrl.parse(urlWithVars)).newBuilder();
        }
    }

    @Override
    public HttpRequestBuilderImpl header(String name, String value) {
        requestBuilder = requestBuilder.header(name, value);
        return this;
    }

    @Override
    public HttpRequestBuilderImpl query(String name, String value) {
        urlBuilder = urlBuilder.addQueryParameter(name, value);
        return this;
    }

    @Override
    public HttpRequestBuilderImpl body(Object body) {
        if (body instanceof String strBody) {
            requestBuilder = requestBuilder.method(method, RequestBody.create(strBody.getBytes(StandardCharsets.UTF_8)));
        } else if (body instanceof byte[] bytesBody) {
            requestBuilder = requestBuilder.method(method, RequestBody.create(bytesBody));
        } else {
            requestBuilder = requestBuilder.method(method, RequestBody.create(writeJson(body).getBytes(StandardCharsets.UTF_8), MediaType.parse("application/json")));
        }
        return this;
    }

    private String writeJson(Object object) {
        try {
            return restClient.config().getObjectMapper().writeValueAsString(object);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public HttpResponse<Void> asEmpty() {
        return doRequest(bodyBytes -> null);
    }

    @Override
    public HttpResponse<String> asString() {
        return doRequest(bodyBytes -> {
            if (bodyBytes == null) {
                return null;
            } else {
                return new String(bodyBytes, StandardCharsets.UTF_8);
            }
        });
    }

    @Override
    public <T> HttpResponse<T> asObject(Class<T> type) {
        return doRequest(bodyBytes -> {
            if (bodyBytes == null) {
                return null;
            } else {
                return restClient.config().getObjectMapper().readValue(new String(bodyBytes, StandardCharsets.UTF_8), type);
            }
        });
    }

    @Override
    public <T> HttpResponse<T> asObject(TypeReference<T> typeReference) {
        return doRequest(bodyBytes -> {
            if (bodyBytes == null) {
                return null;
            } else {
                return restClient.config().getObjectMapper().readValue(new String(bodyBytes, StandardCharsets.UTF_8), typeReference);
            }
        });
    }

    @Override
    public HttpResponse<JsonNode> asJson() {
        return doRequest(bodyBytes -> {
            if (bodyBytes == null) {
                return null;
            } else {
                return restClient.config().getObjectMapper().readTree(new String(bodyBytes, StandardCharsets.UTF_8));
            }
        });
    }

    @Override
    public HttpResponse<byte[]> asBytes() {
        return doRequest(bodyBytes -> bodyBytes);
    }

    private interface ResponseBodyMapper<T> {
        T map(byte[] bodyBytes) throws Exception;
    }

    private <T> HttpResponse<T> doRequest(ResponseBodyMapper<T> responseBodyMapper) {
        Request request = requestBuilder.url(urlBuilder.build()).build();
        try (Response response = restClient.config().getOkHttpClient().newCall(request).execute()) {
            T body = null;
            boolean success = response.isSuccessful();
            byte[] bodyBytes = getBodyBytes(response);
            if (success) {
                if (bodyBytes != null) {
                    body = responseBodyMapper.map(bodyBytes);
                }
            }
            return new HttpResponseImpl<>(restClient, body, response.code(), response.message(), success, success ? null : bodyBytes);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private byte[] getBodyBytes(Response response) throws IOException {
        var responseBody = response.body();
        if (responseBody != null) {
            return responseBody.bytes();
        } else {
            return null;
        }
    }

}
