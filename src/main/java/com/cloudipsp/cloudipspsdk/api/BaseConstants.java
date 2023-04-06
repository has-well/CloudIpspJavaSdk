package com.cloudipsp.cloudipspsdk.api;

import java.net.HttpURLConnection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class BaseConstants {
    public static final Map<String, Integer> serviceErrors;
    public static final int RESPONSE_STATUS_CODE = 0;
    public static final String endpoint = "pay.fondy.eu";
    public static final String sdkHeader = "java-cloudipsp-sdk-clien";
    public static final int RESPONSE_STRING = 1;
    public static final String DELIMITER = "|";
    public static final Integer MAX_CLIENT_CONNECTIONS = 20;

    static {
        Map<String, Integer> serviceErrorsMap = new HashMap<>();
        serviceErrorsMap.put("Internal Server Error", HttpURLConnection.HTTP_INTERNAL_ERROR);
        serviceErrorsMap.put("Service Unavailable", HttpURLConnection.HTTP_UNAVAILABLE);
        serviceErrorsMap.put("Too Many Requests", 429);
        serviceErrorsMap.put("HTTP Bad Gateway", HttpURLConnection.HTTP_BAD_GATEWAY);
        serviceErrorsMap.put("HTTP Gateway Timeout", HttpURLConnection.HTTP_GATEWAY_TIMEOUT);
        serviceErrorsMap.put("Request Timeout", HttpURLConnection.HTTP_CLIENT_TIMEOUT);
        serviceErrors = Collections.unmodifiableMap(serviceErrorsMap);
    }
}
