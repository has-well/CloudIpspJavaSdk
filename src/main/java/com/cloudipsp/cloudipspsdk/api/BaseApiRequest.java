package com.cloudipsp.cloudipspsdk.api;

import com.cloudipsp.cloudipspsdk.Utils;
import com.cloudipsp.cloudipspsdk.exceptions.CloudIpspException;
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
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

public class BaseApiRequest {
    final protected Configuration configuration;

    public BaseApiRequest(final Configuration configuration) throws CloudIpspException {
        this.configuration = configuration;
    }

    /**
     *
     * @param retryCount number of retry
     * @return long
     */
    private long getExponentialWaitTime(int retryCount) {
        return ((long) Math.pow(2, retryCount) * 1000L);
    }

    public BaseApiResponse callAPI(final URI uri,
                                   final String httpMethodName,
                                   final JSONObject request) throws CloudIpspException {
        return processRequest(uri, request.toString(), httpMethodName);
    }

    /**
     * Processes an HTTP request and handles retries if necessary.
     *
     * @param uri            The URI of the request.
     * @param payload        The request payload as a string.
     * @param httpMethodName The HTTP method to use for the request.
     * @return A BaseApiResponse containing the API response.
     * @throws CloudIpspException If there's an error during request processing.
     */
    private BaseApiResponse processRequest(final URI uri,
                                           final String payload,
                                           final String httpMethodName) throws CloudIpspException {
        final BaseApiResponse responseObject = new BaseApiResponse();
        responseObject.setUrl(uri);
        responseObject.setMethod(httpMethodName);

        final Map<String, String> headers = Utils.getDefaultHeaders();
        responseObject.setHeaders(headers);
        responseObject.setRawRequest(payload);

        try {
            long millisBefore = System.currentTimeMillis();
            List<String> response = executeRequestWithRetries(uri, payload, httpMethodName, headers);
            responseObject.setDuration(System.currentTimeMillis() - millisBefore);

            int statusCode = Integer.parseInt(response.get(BaseConstants.RESPONSE_STATUS_CODE));
            responseObject.setStatus(statusCode);

            String rawResponseObject = response.get(BaseConstants.RESPONSE_STRING);
            if (!StringUtils.isEmpty(rawResponseObject)) {
                JSONObject jsonResponse = new JSONObject(rawResponseObject);
                responseObject.setResponse(jsonResponse);
                responseObject.setRawResponse(rawResponseObject);
                processResponseErrors(jsonResponse);
            }
        } catch (InterruptedException | JSONException e) {
            throw new CloudIpspException(e.getMessage(), null, null);
        }

        return responseObject;
    }

    /**
     * Executes an HTTP request with retries in case of server errors.
     *
     * @param uri            The URI of the request.
     * @param payload        The request payload as a string.
     * @param httpMethodName The HTTP method to use for the request.
     * @param headers        The request headers as a map.
     * @return A list containing the response status code and response body.
     * @throws InterruptedException If the thread is interrupted during sleep.
     * @throws CloudIpspException   If there's an error during request execution.
     */
    private List<String> executeRequestWithRetries(final URI uri,
                                                   final String payload,
                                                   final String httpMethodName,
                                                   final Map<String, String> headers)
            throws InterruptedException, CloudIpspException {
        List<String> response = sendRequest(uri, payload, httpMethodName, headers);
        int statusCode = Integer.parseInt(response.get(BaseConstants.RESPONSE_STATUS_CODE));
        int retry = 0;

        while (BaseConstants.serviceErrors.containsValue(statusCode) && retry < configuration.getMaxRetries()) {
            retry++;
            long waitTime = getExponentialWaitTime(retry);
            Thread.sleep(waitTime);
            response = sendRequest(uri, payload, httpMethodName, headers);
            statusCode = Integer.parseInt(response.get(BaseConstants.RESPONSE_STATUS_CODE));
        }

        return response;
    }

    /**
     * Processes the JSON response and throws an exception if it contains errors.
     *
     * @param jsonResponse The JSON response object.
     * @throws CloudIpspException If the response contains an error.
     */
    private void processResponseErrors(JSONObject jsonResponse) throws CloudIpspException {
        if (jsonResponse.get("response") instanceof JSONObject) {
            JSONObject resp = jsonResponse.getJSONObject("response");
            if (resp.has("error_code")) {
                throw new CloudIpspException(resp.getString("error_message"),
                        Integer.toString(resp.getInt("error_code")),
                        resp.getString("request_id"));
            }
        }
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
                                     final Map<String, String> headers) throws CloudIpspException {
        final List<String> result = new ArrayList<>();
        final StringBuilder response = new StringBuilder();
        int responseCode;
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
            throw new CloudIpspException(exception);
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
     * @throws CloudIpspException basic
     */
    private static HttpUriRequest getHttpUriRequest(final URI uri, final String httpMethodName, final String payload)
            throws CloudIpspException {
        switch (httpMethodName) {
            case "GET":
                return new HttpGet(uri);
            case "POST":
                final HttpPost httpPost = new HttpPost(uri);
                httpPost.setEntity(new StringEntity(payload, StandardCharsets.UTF_8));
                return httpPost;
            default:
                throw new CloudIpspException("Invalid HTTP method " + httpMethodName, null, null);
        }
    }

    /**
     * generate full request by version
     *
     * @param request client params
     * @return full request
     */
    public JSONObject fullRequest(JSONObject request, String key, boolean appendVersion) {
        if (!request.has("merchant_id")) {
            request.put("merchant_id", configuration.getMerchantId());
        }
        if (request.has("amount") || request.get("amount") instanceof Float) {
            request.put("amount", request.getFloat("amount") * 100);
        }
        JSONObject paymentRequest = new JSONObject();
        String protocolVersion = configuration.getVersion();
        if (protocolVersion.equals("2.0")) {
            JSONObject dataV2 = new JSONObject();
            dataV2.put("order", request);
            String encodedData = Utils.toBase64(dataV2.toString());
            paymentRequest.put("data", encodedData);
            paymentRequest.put("signature", Utils.generateSignatureV2(encodedData, key));
            paymentRequest.put("version", protocolVersion);
        } else {
            if (appendVersion) {
                request.put("version", protocolVersion);
            }
            request.put("signature", Utils.generateSignature(request, key));
            paymentRequest = request;
        }

        JSONObject finalRequest = new JSONObject();
        finalRequest.put("request", paymentRequest);
        return finalRequest;
    }

    /**
     * Checks if the required parameter is present in the JSON object.
     *
     * @param paymentRequest The JSON object containing the payment request details.
     * @param parameterName  The name of the required parameter.
     * @throws CloudIpspException If the required parameter is missing.
     */
    public void checkRequiredParameter(JSONObject paymentRequest, String parameterName) throws CloudIpspException {
        if (!paymentRequest.has(parameterName)) {
            throw new CloudIpspException("param " + parameterName + " is required", "1000", null);
        }
    }
}
