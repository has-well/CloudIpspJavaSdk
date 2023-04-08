package com.cloudipsp.cloudipspsdk.api;

import com.cloudipsp.cloudipspsdk.CloudIpspConfiguration;
import com.cloudipsp.cloudipspsdk.Utils;
import com.cloudipsp.cloudipspsdk.exceptions.CloudIpspException;
import org.json.JSONObject;

import java.net.URI;

public class PciDss extends BaseApiRequest {
    public PciDss(CloudIpspConfiguration configuration) throws CloudIpspException {
        super(configuration);
    }

    /**
     * Processes a p2pCredit request.
     *
     * @param paymentRequest The JSON object containing the request details.
     * @return A BaseApiResponse containing the API response.
     * @throws CloudIpspException If the credit key is missing or any other exception occurs during processing.
     */
    public BaseApiResponse p2pCredit(final JSONObject paymentRequest) throws CloudIpspException {
        if (configuration.getCreditKey() == null) {
            throw new CloudIpspException("Credit Key is required", "1000", null);
        }
        final URI payUrl = Utils.getServiceURI(configuration, "/p2pcredit/");
        JSONObject request = prepareRequest(paymentRequest, configuration.getCreditKey());
        return callAPI(payUrl, "POST", request);
    }

    /**
     * Processes a request.
     *
     * @param paymentRequest The JSON object containing the request details.
     * @return A BaseApiResponse containing the API response.
     * @throws CloudIpspException If the credit key is missing or any other exception occurs during processing.
     */
    public BaseApiResponse stepOne(final JSONObject paymentRequest) throws CloudIpspException {
        final URI payUrl = Utils.getServiceURI(configuration, "/3dsecure_step1/");
        checkRequiredParameter(paymentRequest, "card_number");
        checkRequiredParameter(paymentRequest, "cvv2");
        checkRequiredParameter(paymentRequest, "expiry_date");
        JSONObject request = prepareRequest(paymentRequest, configuration.getSecretKey());
        return callAPI(payUrl, "POST", request);
    }

    /**
     * Processes a request.
     *
     * @param paymentRequest The JSON object containing the request details.
     * @return A BaseApiResponse containing the API response.
     * @throws CloudIpspException If the credit key is missing or any other exception occurs during processing.
     */
    public BaseApiResponse stepTwo(final JSONObject paymentRequest) throws CloudIpspException {
        final URI payUrl = Utils.getServiceURI(configuration, "/3dsecure_step2/");
        JSONObject request = prepareRequest(paymentRequest, configuration.getSecretKey());
        return callAPI(payUrl, "POST", request);
    }

    /**
     * preparing request
     *
     * @param request JSONObject
     * @return JSONObject
     */
    private JSONObject prepareRequest(JSONObject request, String key) {

        if (!request.has("order_id")) {
            request.put("order_id", Utils.generateOrderID());
        }
        if (!request.has("order_desc")) {
            request.put("order_desc", Utils.generateOrderDesc(request.getString("order_id")));
        }

        return fullRequest(request, key, true);
    }


}
