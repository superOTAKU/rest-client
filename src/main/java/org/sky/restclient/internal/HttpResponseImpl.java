package org.sky.restclient.internal;

import com.fasterxml.jackson.core.type.TypeReference;
import org.sky.restclient.HttpResponse;
import org.sky.restclient.RestClient;

import java.nio.charset.StandardCharsets;
import java.util.Optional;
import java.util.function.Consumer;

public class HttpResponseImpl<T> implements HttpResponse<T> {
    private final RestClient restClient;
    private final T body;
    private final int code;
    private final String msg;
    private final byte[] errBytes;
    private final boolean success;

    public HttpResponseImpl(RestClient restClient, T body, int code, String msg, boolean success, byte[] errBytes) {
        this.restClient = restClient;
        this.body = body;
        this.code = code;
        this.msg = msg;
        this.success = success;
        this.errBytes = errBytes;
    }

    @Override
    public int code() {
        return code;
    }

    @Override
    public String msg() {
        return msg;
    }

    @Override
    public T body() {
        return body;
    }

    @Override
    public boolean isSuccessful() {
        return success;
    }

    @Override
    public boolean isError() {
        return !success;
    }

    @Override
    public byte[] errBytes() {
        return errBytes;
    }

    @Override
    public String errString() {
        return Optional.ofNullable(errBytes()).map(e -> new String(e, StandardCharsets.UTF_8)).orElse(null);
    }

    @Override
    public <E> E errObject(Class<E> type) {
        return Optional.ofNullable(errBytes).map(e -> {
            try {
                return restClient.config().getObjectMapper().readValue(new String(e, StandardCharsets.UTF_8), type);
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        }).orElse(null);
    }

    @Override
    public <E> E errObject(TypeReference<E> typeReference) {
        return Optional.ofNullable(errBytes).map(e -> {
            try {
                return restClient.config().getObjectMapper().readValue(new String(e, StandardCharsets.UTF_8), typeReference);
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        }).orElse(null);
    }

    @Override
    public HttpResponse<T> onError(Consumer<HttpResponse<T>> errorHandler) {
        errorHandler.accept(this);
        return this;
    }

    @Override
    public String toString() {
        return "HttpResponse{" +
                "code=" + code +
                ", msg='" + msg + '\'' +
                '}';
    }
}
