package com.cloudipsp.cloudipspsdk.api;

import com.cloudipsp.cloudipspsdk.CloudIpspConfiguration;
import com.cloudipsp.cloudipspsdk.Utils;
import com.cloudipsp.cloudipspsdk.exceptions.CloudIpspException;
import org.json.JSONObject;

import java.net.URI;

public class PaymentApi extends BaseApiRequest {

    public PaymentApi(CloudIpspConfiguration configuration) throws CloudIpspException {
        super(configuration);
    }

    /**
     * Processes a request.
     *
     * @param paymentRequest The JSON object containing the request details.
     * @return A BaseApiResponse containing the API response.
     * @throws CloudIpspException If the credit key is missing or any other exception occurs during processing.
     */
    public BaseApiResponse paymentUrl(final JSONObject paymentRequest) throws CloudIpspException {
        final URI payUrl = Utils.getServiceURI(configuration, "/checkout/url/");
        JSONObject request = prepareRequest(paymentRequest);
        return callAPI(payUrl, "POST", request);
    }

    /**
     * Processes a request.
     *
     * @param paymentRequest The JSON object containing the request details.
     * @return A BaseApiResponse containing the API response.
     * @throws CloudIpspException If the credit key is missing or any other exception occurs during processing.
     */
    public BaseApiResponse paymentVerificationUrl(final JSONObject paymentRequest) throws CloudIpspException {
        final URI payUrl = Utils.getServiceURI(configuration, "/checkout/url/");
        paymentRequest.put("verification", "Y");
        if (!paymentRequest.has("verification_type")) {
            paymentRequest.put("verification_type", "amount");
        }
        if (!paymentRequest.has("amount")) {
            paymentRequest.put("amount", 0);
        }
        JSONObject request = prepareRequest(paymentRequest);
        return callAPI(payUrl, "POST", request);
    }

    /**
     * Processes a request.
     *
     * @param paymentRequest The JSON object containing the request details.
     * @return A BaseApiResponse containing the API response.
     * @throws CloudIpspException If the credit key is missing or any other exception occurs during processing.
     */
    public BaseApiResponse paymentToken(final JSONObject paymentRequest) throws CloudIpspException {
        final URI payUrl = Utils.getServiceURI(configuration, "/checkout/token/");
        JSONObject request = prepareRequest(paymentRequest);
        return callAPI(payUrl, "POST", request);
    }

    /**
     * Processes a request.
     *
     * @param paymentRequest The JSON object containing the request details.
     * @return A BaseApiResponse containing the API response.
     * @throws CloudIpspException If the credit key is missing or any other exception occurs during processing.
     */
    public BaseApiResponse paymentSubscriptions(final JSONObject paymentRequest) throws CloudIpspException {
        configuration.setVersion("2.0");
        paymentRequest.put("subscription", "Y");
        final URI payUrl = Utils.getServiceURI(configuration, "/checkout/url/");
        JSONObject request = prepareRequest(paymentRequest);
        return callAPI(payUrl, "POST", request);
    }

    /**
     * Processes a request.
     *
     * @param paymentRequest The JSON object containing the request details.
     * @return A BaseApiResponse containing the API response.
     * @throws CloudIpspException If the credit key is missing or any other exception occurs during processing.
     */
    public BaseApiResponse paymentSplit(final JSONObject paymentRequest) throws CloudIpspException {
        configuration.setVersion("2.0");
        checkRequiredParameter(paymentRequest, "receiver");
        checkRequiredParameter(paymentRequest, "operation_id");
        final URI payUrl = Utils.getServiceURI(configuration, "/settlement/");
        JSONObject request = prepareRequest(paymentRequest);
        return callAPI(payUrl, "POST", request);
    }

    /**
     * Processes a request.
     *
     * @param paymentRequest The JSON object containing the request details.
     * @return A BaseApiResponse containing the API response.
     * @throws CloudIpspException If the credit key is missing or any other exception occurs during processing.
     */
    public BaseApiResponse paymentRecurrig(final JSONObject paymentRequest) throws CloudIpspException {
        final URI payUrl = Utils.getServiceURI(configuration, "/recurring/");
        checkRequiredParameter(paymentRequest, "rectoken");
        JSONObject request = prepareRequest(paymentRequest);
        return callAPI(payUrl, "POST", request);
    }

    /**
     * preparing request
     *
     * @param request JSONObject
     * @return JSONObject
     */
    private JSONObject prepareRequest(JSONObject request) {

        if (!request.has("order_id")) {
            request.put("order_id", Utils.generateOrderID());
        }
        if (!request.has("order_desc")) {
            request.put("order_desc", Utils.generateOrderDesc(request.getString("order_id")));
        }

        return fullRequest(request, configuration.getSecretKey(), true);
    }
}
