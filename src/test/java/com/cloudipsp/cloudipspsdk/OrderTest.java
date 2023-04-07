package com.cloudipsp.cloudipspsdk;

import com.cloudipsp.cloudipspsdk.api.BaseApiResponse;
import com.cloudipsp.cloudipspsdk.api.OrderApi;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;

public class OrderTest {
    private OrderApi orderApi;

    @Before
    public void setUp() {
        // Load private key and configure client
        final Configuration config = new Configuration()
                .setSecretKey("test")
                .setMerchantId(1396424);
        CloudIpspApi client = new CloudIpspApi(config);
        orderApi = client.getOrderApi();
    }

    @Test
    public void testOrderCapture() {
        JSONObject payload = new JSONObject();
        payload.put("currency", "EUR");
        payload.put("amount", 100);
        payload.put("order_id", "test123");
        BaseApiResponse response = orderApi.Capture(payload);
        assert response.isSuccess();
        JSONObject order = response.getParsedResponse();
        assert order.getString("capture_status").equals("captured");
    }

    @Test
    public void testOrderReverse() {
        JSONObject payload = new JSONObject();
        payload.put("currency", "EUR");
        payload.put("amount", 100);
        payload.put("order_id", "test123");
        BaseApiResponse response = orderApi.Reverse(payload);
        assert response.isSuccess();
        JSONObject order = response.getParsedResponse();
        assert order.getString("reverse_status").equals("approved");
    }

    @Test
    public void testOrderStatus() {
        JSONObject payload = new JSONObject();
        payload.put("order_id", "test123");
        BaseApiResponse response = orderApi.Status(payload);
        assert response.isSuccess();
        JSONObject order = response.getParsedResponse();
        assert order.getString("order_status").equals("expired");
    }

    @Test
    public void testTransactionList() {
        JSONObject payload = new JSONObject();
        payload.put("order_id", "test1000");
        BaseApiResponse response = orderApi.TransactionList(payload);
        assert response.isSuccess();
        JSONArray order = response.getTransactionList();
        assert order.length() == 1;
    }


}
