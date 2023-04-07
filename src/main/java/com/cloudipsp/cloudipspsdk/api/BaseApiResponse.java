package com.cloudipsp.cloudipspsdk.api;

import com.cloudipsp.cloudipspsdk.Utils;
import com.cloudipsp.cloudipspsdk.exceptions.CloudIpspException;
import org.json.JSONArray;
import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.net.URI;
import java.util.Map;


public class BaseApiResponse {
    private URI url;
    private String method;
    private String rawRequest;
    private JSONObject response;
    private String rawResponse;
    private int status;
    private int retries;
    private long duration;
    private Map<String, String> headers;

    /**
     * @return Returns the url of the API request
     */
    public URI getUrl() {
        return url;
    }

    /**
     * @param url is the Request url of type URI
     */
    public void setUrl(final URI url) {
        this.url = url;
    }

    /**
     * @return Returns the checkout url
     */
    public URI getCheckoutUrl() throws CloudIpspException {
        JSONObject response = this.getParsedResponse();
        if (!response.has("checkout_url")){
            throw new CloudIpspException("checkout_url not in response", null, null);
        }
        return URI.create(response.getString("checkout_url"));
    }

    /**
     * @return Returns the HTTP Method type
     */
    public String getMethod() {
        return method;
    }

    /**
     * @param method is the HTTP method invoked
     */
    public void setMethod(final String method) {
        this.method = method;
    }

    /**
     * @return Returns the Request payload
     */
    public String getRawRequest() {
        return rawRequest;
    }

    /**
     * @param rawRequest is the Request payload
     */
    public void setRawRequest(final String rawRequest) {
        this.rawRequest = rawRequest;
    }

    /**
     * @return Returns the JSON Response from Response
     */
    public JSONObject getResponse() {
        if (!response.get("response").toString().isEmpty()) {
            return response.getJSONObject("response");
        } else {
            return response;
        }
    }

    public JSONArray getTransactionList() {
        return response.getJSONArray("response");
    }

    /**
     * @return Returns the JSON Response from Response
     */
    public JSONObject getParsedResponse() {
        JSONObject resp = getResponse();
        if (resp.has("data")){
            return new JSONObject(Utils.fromBase64(resp.getString("data"))).getJSONObject("order");
        }
        return resp;
    }

    /**
     * @param response is Response from API call of type JSON
     */
    public void setResponse(final JSONObject response) {
        this.response = response;
    }

    /**
     * @return Returns the Raw Response from the API call
     */
    public String getRawResponse() {
        return rawResponse;
    }

    /**
     * @param rawResponse is the Raw Response returned from API call
     */
    public void setRawResponse(final String rawResponse) {
        this.rawResponse = rawResponse;
    }

    /**
     * @return Returns the Http Status Code from the response
     */
    public int getStatus() {
        return status;
    }

    /**
     * @param status Http Status Code of type int
     */
    public void setStatus(final int status) {
        this.status = status;
    }

    /**
     * @return Returns no. of retries to run the request
     */
    public int getRetries() {
        return retries;
    }

    /**
     * @param retries no. of retries to run the request
     */
    public void setRetries(final int retries) {
        this.retries = retries;
    }

    /**
     * @return Returns the time taken to run the request in milliseconds
     */
    public long getDuration() {
        return duration;
    }

    /**
     * @param duration the time taken to run the request in milliseconds
     */
    public void setDuration(final long duration) {
        this.duration = duration;
    }

    /**
     * @return Returns the headers provided to the request
     */
    public Map<String, String> getHeaders() {
        return headers;
    }

    /**
     * @param headers the headers provided to the request
     */
    public void setHeaders(final Map<String, String> headers) {
        this.headers = headers;
    }

    /**
     * @return Returns boolean response
     */
    public boolean isSuccess() {
        return status == HttpURLConnection.HTTP_OK;
    }

    /**
     * @return Provide troubleshooting information to developer in easy to read form
     */
    @Override
    public String toString() {
        return "Response{"
                + "status=" + status
                + ", success=" + isSuccess()
                + ", duration=" + duration
                + ", retries=" + retries
                + ", method=" + method
                + ", url= " + url
                + ", headers=" + headers
                + ", rawRequest=" + rawRequest
                + ", rawResponse=" + rawResponse + '}';
    }

}
