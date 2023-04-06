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
     * @param requset
     * @return JSONObject
     */
    private JSONObject prepareRequest(JSONObject requset) {
        JSONObject paymentRequest = new JSONObject();
        if (!requset.has("merchant_id")) {
            requset.put("merchant_id", configuration.getMerchantId());
        }
        if (!requset.has("order_id")) {
            requset.put("order_id", Utils.generateOrderID());
        }
        if (!requset.has("order_desc")) {
            requset.put("order_desc", Utils.generateOrderDesc(requset.getString("order_id")));
        }
        requset.put("signature", Utils.generateSignature(requset, configuration.getSecretKey()));
        paymentRequest.put("request", requset);
        return paymentRequest;
    }

    @Override
    public String getSdkVersion() {
        return "1.0.0";
    }
}
