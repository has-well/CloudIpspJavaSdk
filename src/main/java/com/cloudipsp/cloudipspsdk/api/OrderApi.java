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
     * @param paymentRequest client request
     * @return api response
     * @throws CloudIpspException basic
     */
    public BaseApiResponse Capture(final JSONObject paymentRequest) throws CloudIpspException {
        final URI payUrl = Utils.getServiceURI(configuration, "/capture/order_id/");
        if (!paymentRequest.has("order_id")) {
            throw new CloudIpspException("param order_id is required", null, null);
        }
        JSONObject request = prepareRequest(paymentRequest, true);
        return callAPI(payUrl, "POST", request.toString());
    }

    /**
     * @param paymentRequest client request
     * @return api response
     * @throws CloudIpspException basic
     */
    public BaseApiResponse Reverse(final JSONObject paymentRequest) throws CloudIpspException {
        final URI payUrl = Utils.getServiceURI(configuration, "/reverse/order_id/");
        if (!paymentRequest.has("order_id")) {
            throw new CloudIpspException("param order_id is required", null, null);
        }
        JSONObject request = prepareRequest(paymentRequest, true);
        return callAPI(payUrl, "POST", request.toString());
    }

    /**
     * @param paymentRequest client request
     * @return api response
     * @throws CloudIpspException basic
     */
    public BaseApiResponse Status(final JSONObject paymentRequest) throws CloudIpspException {
        final URI payUrl = Utils.getServiceURI(configuration, "/status/order_id/");
        if (!paymentRequest.has("order_id")) {
            throw new CloudIpspException("param order_id is required", null, null);
        }
        JSONObject request = prepareRequest(paymentRequest, true);
        return callAPI(payUrl, "POST", request.toString());
    }

    /**
     * @param paymentRequest client request
     * @return api response
     * @throws CloudIpspException basic
     */
    public BaseApiResponse TransactionList(final JSONObject paymentRequest) throws CloudIpspException {
        final URI payUrl = Utils.getServiceURI(configuration, "/transaction_list/");
        if (!paymentRequest.has("order_id")) {
            throw new CloudIpspException("param order_id is required", null, null);
        }
        JSONObject request = prepareRequest(paymentRequest, false);
        return callAPI(payUrl, "POST", request.toString());
    }

    /**
     * preparing request
     *
     * @param request JSONObject
     * @return JSONObject
     */
    private JSONObject prepareRequest(JSONObject request, boolean appendVersion) {

        if (!request.has("merchant_id")) {
            request.put("merchant_id", configuration.getMerchantId());
        }

        return fullRequest(request, appendVersion);
    }
}
