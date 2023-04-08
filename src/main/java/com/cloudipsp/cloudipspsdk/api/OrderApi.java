package com.cloudipsp.cloudipspsdk.api;

import com.cloudipsp.cloudipspsdk.Configuration;
import com.cloudipsp.cloudipspsdk.Utils;
import com.cloudipsp.cloudipspsdk.exceptions.CloudIpspException;
import org.json.JSONObject;

import java.net.URI;

public class OrderApi extends BaseApiRequest {
    public OrderApi(Configuration configuration) throws CloudIpspException {
        super(configuration);
    }

    /**
     * Processes a request.
     *
     * @param paymentRequest The JSON object containing the request details.
     * @return A BaseApiResponse containing the API response.
     * @throws CloudIpspException If the credit key is missing or any other exception occurs during processing.
     */
    public BaseApiResponse Capture(final JSONObject paymentRequest) throws CloudIpspException {
        final URI payUrl = Utils.getServiceURI(configuration, "/capture/order_id/");
        checkInput(paymentRequest);
        JSONObject request = fullRequest(paymentRequest, configuration.getSecretKey(), true);
        return callAPI(payUrl, "POST", request);
    }

    /**
     * Processes a request.
     *
     * @param paymentRequest The JSON object containing the request details.
     * @return A BaseApiResponse containing the API response.
     * @throws CloudIpspException If the credit key is missing or any other exception occurs during processing.
     */
    public BaseApiResponse Reverse(final JSONObject paymentRequest) throws CloudIpspException {
        final URI payUrl = Utils.getServiceURI(configuration, "/reverse/order_id/");
        checkInput(paymentRequest);
        JSONObject request = fullRequest(paymentRequest, configuration.getSecretKey(), true);
        return callAPI(payUrl, "POST", request);
    }

    /**
     * Processes a request.
     *
     * @param paymentRequest The JSON object containing the request details.
     * @return A BaseApiResponse containing the API response.
     * @throws CloudIpspException If the credit key is missing or any other exception occurs during processing.
     */
    public BaseApiResponse Status(final JSONObject paymentRequest) throws CloudIpspException {
        final URI payUrl = Utils.getServiceURI(configuration, "/status/order_id/");
        checkInput(paymentRequest);
        JSONObject request = fullRequest(paymentRequest, configuration.getSecretKey(), true);
        return callAPI(payUrl, "POST", request);
    }

    /**
     * Processes a request.
     *
     * @param paymentRequest The JSON object containing the request details.
     * @return A BaseApiResponse containing the API response.
     * @throws CloudIpspException If the credit key is missing or any other exception occurs during processing.
     */
    public BaseApiResponse TransactionList(final JSONObject paymentRequest) throws CloudIpspException {
        final URI payUrl = Utils.getServiceURI(configuration, "/transaction_list/");
        checkInput(paymentRequest);
        JSONObject request = fullRequest(paymentRequest, configuration.getSecretKey(), false);
        return callAPI(payUrl, "POST", request);
    }

    /**
     * Processes a request.
     *
     * @param paymentRequest The JSON object containing the request details.
     * @return A BaseApiResponse containing the API response.
     * @throws CloudIpspException If the credit key is missing or any other exception occurs during processing.
     */
    public BaseApiResponse SubscriptionStop(final JSONObject paymentRequest) throws CloudIpspException {
        final URI payUrl = Utils.getServiceURI(configuration, "/subscription/");
        paymentRequest.put("action", "stop");
        checkInput(paymentRequest);
        JSONObject request = fullRequest(paymentRequest, configuration.getSecretKey(), false);
        return callAPI(payUrl, "POST", request);
    }

    /**
     * Processes a request.
     *
     * @param paymentRequest The JSON object containing the request details.
     * @return A BaseApiResponse containing the API response.
     * @throws CloudIpspException If the credit key is missing or any other exception occurs during processing.
     */
    public BaseApiResponse Reports(final JSONObject paymentRequest) throws CloudIpspException {
        final URI payUrl = Utils.getServiceURI(configuration, "/reports/");
        checkRequiredParameter(paymentRequest, "date_from");
        checkRequiredParameter(paymentRequest, "date_to");
        JSONObject request = fullRequest(paymentRequest, configuration.getSecretKey(), false);
        return callAPI(payUrl, "POST", request);
    }

    /**
     * Check if required params present
     *
     * @param paymentRequest client request
     * @throws CloudIpspException basic
     */
    private void checkInput(JSONObject paymentRequest) throws CloudIpspException {
        if (configuration.getSecretKey() == null) {
            throw new CloudIpspException("Secret Key is required", "1000", null);
        }
        checkRequiredParameter(paymentRequest, "order_id");
    }
}
