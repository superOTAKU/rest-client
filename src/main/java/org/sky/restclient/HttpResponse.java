package org.sky.restclient;

import com.fasterxml.jackson.core.type.TypeReference;

import java.util.function.Consumer;

public interface HttpResponse<T> {

    int code();

    String msg();

    T body();

    boolean isSuccessful();

    boolean isError();

    byte[] errBytes();

    String errString();

    <E> E errObject(Class<E> type);

    <E> E errObject(TypeReference<E> typeReference);

    HttpResponse<T> onError(Consumer<HttpResponse<T>> errorHandler);

}
