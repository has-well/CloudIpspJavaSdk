package com.cloudipsp.cloudipspsdk;

import com.cloudipsp.cloudipspsdk.api.BaseApiRequest;
import com.cloudipsp.cloudipspsdk.api.BaseApiResponse;
import com.cloudipsp.cloudipspsdk.exceptions.CloudipspException;

import org.json.JSONObject;

import java.net.URI;

public class CloudIpspApi extends BaseApiRequest implements CloudIpspSdk {

    public CloudIpspApi(Configuration configuration) throws CloudipspException {
        super(configuration);
    }

    public BaseApiResponse paymentUrl(final JSONObject paymentRequest) throws CloudipspException {
        final URI payUrl = Utils.getServiceURI(configuration, "/checkout/url/");
        JSONObject request = prepareRequest(paymentRequest);
        return callAPI(payUrl, "POST", request.toString());
    }

    /**
     * preparing request
     *
     * @param req JSONObject
     * @return JSONObject
     */
    private JSONObject prepareRequest(JSONObject req) {
        JSONObject paymentRequest = new JSONObject();
        if (!req.has("merchant_id")) {
            req.put("merchant_id", configuration.getMerchantId());
        }
        if (!req.has("order_id")) {
            req.put("order_id", Utils.generateOrderID());
        }
        if (!req.has("order_desc")) {
            req.put("order_desc", Utils.generateOrderDesc(requset.getString("order_id")));
        }
        req.put("signature", Utils.generateSignature(requset, configuration.getSecretKey()));
        paymentRequest.put("request", requset);
        return paymentRequest;
    }

    @Override
    public String getSdkVersion() {
        return "1.0.0";
    }
}
