package com.cloudipsp.cloudipspsdk;

import com.cloudipsp.cloudipspsdk.api.BaseApiResponse;
import com.cloudipsp.cloudipspsdk.exceptions.CloudipspException;
import org.junit.Before;
import org.junit.Test;
import org.json.JSONObject;

import java.util.Objects;

public class GeneratePaymentUrlTest {
    private CloudIpspApi client;

    @Before
    public void setUp() {
        // Load private key and configure client
        final Configuration config = new Configuration()
                .setSecretKey("test")
                .setMerchantId(1396424);
        client = new CloudIpspApi(config);
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
            client.paymentUrl(payload);
        } catch (final CloudipspException e) {
            assert Objects.equals(e.error_code, "1007");
        }
    }

    @Test
    public void testApiCallSuccess() {
        JSONObject payload = new JSONObject();
        payload.put("amount", 100);
        payload.put("currency", "EUR");
        BaseApiResponse response = client.paymentUrl(payload);
        assert response.isSuccess();
    }


}
