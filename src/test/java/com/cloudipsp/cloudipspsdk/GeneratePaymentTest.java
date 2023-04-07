package com.cloudipsp.cloudipspsdk;

import com.cloudipsp.cloudipspsdk.api.BaseApiResponse;
import com.cloudipsp.cloudipspsdk.api.PaymentApi;
import com.cloudipsp.cloudipspsdk.exceptions.CloudIpspException;
import org.junit.Before;
import org.junit.Test;
import org.json.JSONObject;

import java.util.Objects;

public class GeneratePaymentTest {
    private CloudIpspApi client;
    private PaymentApi pamentApi;

    @Before
    public void setUp() {
        // Load private key and configure client
        final Configuration config = new Configuration()
                .setSecretKey("test")
                .setMerchantId(1396424);
        client = new CloudIpspApi(config);
        pamentApi = client.getPaymentApi();
    }

    @Test
    public void testApiVersion() {
        assert Objects.equals(client.getSdkVersion(), "1.0.0");
    }

    @Test
    public void testApiCallFail() {
        JSONObject payload = new JSONObject();
        payload.put("amount", 100.2);
        payload.put("currency", "EUR");
        try {
            pamentApi.paymentUrl(payload);
        } catch (final CloudIpspException e) {
            assert Objects.equals(e.error_code, "1007");
        }
    }

    @Test
    public void testApiCallSuccess() {
        JSONObject payload = new JSONObject();
        payload.put("amount", 100);
        payload.put("currency", "EUR");
        BaseApiResponse response = pamentApi.paymentUrl(payload);
        assert response.isSuccess();
        JSONObject order = response.getParsedResponse();
        assert order.has("checkout_url");
    }

    @Test
    public void testApiCallVerification() {
        JSONObject payload = new JSONObject();
        payload.put("currency", "EUR");
        BaseApiResponse response = pamentApi.paymentVerificationUrl(payload);
        assert response.isSuccess();
        JSONObject order = response.getParsedResponse();
        assert order.has("checkout_url");
    }

    @Test
    public void testApiCallToken() {
        JSONObject payload = new JSONObject();
        payload.put("currency", "EUR");
        payload.put("amount", 100);
        BaseApiResponse response = pamentApi.paymentToken(payload);
        assert response.isSuccess();
        JSONObject order = response.getParsedResponse();
        assert order.has("token");
    }

    @Test
    public void testApiCallSubscriptions() {
        JSONObject payload = new JSONObject();
        payload.put("currency", "EUR");
        payload.put("amount", 100);
        BaseApiResponse response = pamentApi.paymentSubscriptions(payload);
        assert response.isSuccess();
        JSONObject order = response.getParsedResponse();
        assert order.getJSONObject("order").has("checkout_url");
    }


}
