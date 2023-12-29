package org.sky.restclient;

import org.sky.restclient.internal.RestClientImpl;

public class RestClients {
    private static final RestClient DEFAULT_CLIENT = new RestClientImpl();

    public static RestClient getDefault() {
        return DEFAULT_CLIENT;
    }

    public static RestClient newClient() {
        return new RestClientImpl();
    }

}
