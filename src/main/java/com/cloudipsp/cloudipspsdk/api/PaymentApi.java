package com.cloudipsp.cloudipspsdk.api;

import com.cloudipsp.cloudipspsdk.Configuration;
import com.cloudipsp.cloudipspsdk.Utils;
import com.cloudipsp.cloudipspsdk.exceptions.CloudIpspException;
import org.json.JSONObject;

import java.net.URI;

public class PaymentApi extends BaseApiRequest {

    public PaymentApi(Configuration configuration) throws CloudIpspException {
        super(configuration);
    }

    /**
     * Get payment url
     *
     * @param paymentRequest client request
     * @return checkout response
     * @throws CloudIpspException basic
     */
    public BaseApiResponse paymentUrl(final JSONObject paymentRequest) throws CloudIpspException {
        final URI payUrl = Utils.getServiceURI(configuration, "/checkout/url/");
        JSONObject request = prepareRequest(paymentRequest);
        return callAPI(payUrl, "POST", request.toString());
    }

    /**
     * @param paymentRequest client request
     * @return api response
     * @throws CloudIpspException basic
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
        return callAPI(payUrl, "POST", request.toString());
    }

    /**
     * @param paymentRequest client request
     * @return api response
     * @throws CloudIpspException basic
     */
    public BaseApiResponse paymentToken(final JSONObject paymentRequest) throws CloudIpspException {
        final URI payUrl = Utils.getServiceURI(configuration, "/checkout/token/");
        JSONObject request = prepareRequest(paymentRequest);
        return callAPI(payUrl, "POST", request.toString());
    }

    /**
     * @param paymentRequest client request
     * @return api response
     * @throws CloudIpspException basic
     */
    public BaseApiResponse paymentSubscriptions(final JSONObject paymentRequest) throws CloudIpspException {
        configuration.setVersion("2.0");
        paymentRequest.put("subscription", "Y");
        final URI payUrl = Utils.getServiceURI(configuration, "/checkout/url/");
        JSONObject request = prepareRequest(paymentRequest);
        return callAPI(payUrl, "POST", request.toString());
    }

    /**
     * preparing request
     *
     * @param request JSONObject
     * @return JSONObject
     */
    private JSONObject prepareRequest(JSONObject request) {

        if (!request.has("merchant_id")) {
            request.put("merchant_id", configuration.getMerchantId());
        }
        if (!request.has("order_id")) {
            request.put("order_id", Utils.generateOrderID());
        }
        if (!request.has("order_desc")) {
            request.put("order_desc", Utils.generateOrderDesc(request.getString("order_id")));
        }

        return fullRequest(request, true);
    }
}
