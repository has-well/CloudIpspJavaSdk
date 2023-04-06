package com.cloudipsp.cloudipspsdk.api;

import com.cloudipsp.cloudipspsdk.Utils;
import com.cloudipsp.cloudipspsdk.exceptions.CloudipspException;
import com.cloudipsp.cloudipspsdk.Configuration;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.*;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.util.EntityUtils;
import org.apache.commons.lang.StringUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

public class BaseApiRequest {
    final protected Configuration configuration;

    public BaseApiRequest(final Configuration configuration) throws CloudipspException {
        this.configuration = configuration;
    }

    private long getExponentialWaitTime(int retryCount) {
        return ((long) Math.pow(2, retryCount) * 1000L);
    }

    public BaseApiResponse callAPI(final URI uri,
                                   final String httpMethodName,
                                   final String request) throws CloudipspException {
        return processRequest(uri, request, httpMethodName);
    }

    /**
     * Helper method to send the request and also retry in case the request is throttled
     *
     * @param uri            the uri to be executed
     * @param payload        the payload to be sent with the request
     * @param httpMethodName the HTTP request method(GET,PUT,POST etc) to be used
     * @return the BaseApiResponse
     */
    private BaseApiResponse processRequest(final URI uri,
                                           final String payload,
                                           final String httpMethodName) throws CloudipspException {
        List<String> response;
        String rawResponseObject = null;
        JSONObject jsonResponse = null;

        final BaseApiResponse responseObject = new BaseApiResponse();
        responseObject.setUrl(uri);
        final Map<String, String> headers = Utils.getDefaultHeaders();
        responseObject.setMethod(httpMethodName);
        responseObject.setHeaders(headers);
        responseObject.setRawRequest(payload);
        try {
            long millisBefore = System.currentTimeMillis();
            response = sendRequest(uri, payload, httpMethodName, headers);
            int statusCode = Integer.parseInt(response.get(BaseConstants.RESPONSE_STATUS_CODE));
            int retry = 0;
            while (BaseConstants.serviceErrors.containsValue(statusCode) &&
                    retry < configuration.getMaxRetries()) {
                retry++;
                long waitTime = getExponentialWaitTime(retry);
                Thread.sleep(waitTime);
                response = sendRequest(uri, payload, httpMethodName, headers);
                statusCode = Integer.parseInt(response.get(BaseConstants.RESPONSE_STATUS_CODE));
            }
            responseObject.setRetries(retry);
            responseObject.setStatus(statusCode);
            responseObject.setDuration(System.currentTimeMillis() - millisBefore);
            if (response.get(BaseConstants.RESPONSE_STRING) != null) {
                rawResponseObject = response.get(BaseConstants.RESPONSE_STRING);
                if (!StringUtils.isEmpty(response.get(BaseConstants.RESPONSE_STRING))) {
                    jsonResponse = new JSONObject(response.get(BaseConstants.RESPONSE_STRING));
                }
            }
        } catch (InterruptedException | JSONException e) {
            throw new CloudipspException(e.getMessage(), null, null);
        }
        assert jsonResponse != null;
        JSONObject resp = jsonResponse.getJSONObject("response");
        if (resp.has("error_code")) {
            throw new CloudipspException(resp.getString("error_message"),
                    Integer.toString(resp.getInt("error_code")),
                    resp.getString("request_id"));
        }
        responseObject.setResponse(jsonResponse);
        responseObject.setRawResponse(rawResponseObject);

        return responseObject;
    }

    /**
     * Helper method to post the request
     *
     * @param uri            the uri to be executed
     * @param payload        the payload ot be sent with the request
     * @param httpMethodName the HTTP request method(GET,PUT,POST etc) to be used
     * @return the response and response code
     */
    private List<String> sendRequest(final URI uri,
                                     final String payload,
                                     final String httpMethodName,
                                     final Map<String, String> headers) throws CloudipspException {
        final List<String> result = new ArrayList<>();
        final StringBuffer response = new StringBuffer();
        int responseCode = 0;
        try (final CloseableHttpClient client = getHttpClientWithConnectionPool(configuration)) {
            final HttpUriRequest httpUriRequest = getHttpUriRequest(uri, httpMethodName, payload);
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                httpUriRequest.addHeader(entry.getKey(), entry.getValue());
            }
            final HttpResponse responses = client.execute(httpUriRequest);
            responseCode = responses.getStatusLine().getStatusCode();
            if (responseCode < HttpURLConnection.HTTP_BAD_REQUEST) {
                String inputLine;
                try (final BufferedReader in = new BufferedReader(
                        new InputStreamReader(responses.getEntity().getContent(), StandardCharsets.UTF_8))) {
                    while ((inputLine = in.readLine()) != null) {
                        response.append(inputLine).append(System.lineSeparator());
                    }
                }
            } else {
                response.append(EntityUtils.toString(responses.getEntity()));
            }
        } catch (IOException exception) {
            throw new CloudipspException(exception);
        }
        result.add(String.valueOf(responseCode));
        result.add(response.toString());
        return result;
    }

    /**
     * @return CloseableHttpClient
     */
    private static CloseableHttpClient getHttpClientWithConnectionPool(final Configuration configuration) {
        final PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager();
        connectionManager.setMaxTotal(configuration.getClientConnections());
        connectionManager.setDefaultMaxPerRoute(configuration.getClientConnections());
        final HttpClientBuilder httpClientBuilder = HttpClients.custom()
                .setConnectionManager(connectionManager);
        return httpClientBuilder.build();
    }

    /**
     * @return HttpPost
     * @throws CloudipspException
     */
    private static HttpUriRequest getHttpUriRequest(final URI uri, final String httpMethodName, final String payload)
            throws CloudipspException {
        switch (httpMethodName) {
            case "GET":
                return new HttpGet(uri);
            case "POST":
                final HttpPost httpPost = new HttpPost(uri);
                httpPost.setEntity(new StringEntity(payload, StandardCharsets.UTF_8));
                return httpPost;
            default:
                throw new CloudipspException("Invalid HTTP method " + httpMethodName, null, null);
        }
    }

}
