package org.sky.restclient;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;

public interface HttpResponseFetcher {

    HttpResponse<Void> asEmpty();

    HttpResponse<String> asString();

    <T> HttpResponse<T> asObject(Class<T> type);

    <T> HttpResponse<T> asObject(TypeReference<T> typeReference);

    HttpResponse<JsonNode> asJson();

    HttpResponse<byte[]> asBytes();

}
