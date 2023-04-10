package com.cloudipsp.cloudipspsdk;

import com.cloudipsp.cloudipspsdk.api.BaseApiResponse;
import com.cloudipsp.cloudipspsdk.api.OrderApi;
import com.cloudipsp.cloudipspsdk.api.PciDss;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class OrderTest {
    private OrderApi orderApi;
    private PciDss pciDssApi;
    private final Integer amount = 100;
    private final String currency = "UAH";

    @Before
    public void setUp() {
        // Load private key and configure client
        final CloudIpspConfiguration config = new CloudIpspConfiguration()
                .setSecretKey("test")
                .setMerchantId(1396424);
        CloudIpspApi client = new CloudIpspApi(config);
        orderApi = client.getOrderApi();
        pciDssApi = client.getPciDssApi();
    }

    public JSONObject pciDssOrder(boolean preauth) {
        JSONObject payload = new JSONObject();
        payload.put("currency", currency);
        payload.put("amount", amount);
        payload.put("card_number", "4444555511116666");
        payload.put("cvv2", "123");
        payload.put("expiry_date", "1244");
        if (preauth) {
            payload.put("preauth", "Y");
        }

        BaseApiResponse response = pciDssApi.stepOne(payload);
        assert response.isSuccess();

        return response.getParsedResponse();
    }

    @Test
    public void testOrderCapture() {
        JSONObject payload = new JSONObject();
        payload.put("currency", currency);
        payload.put("amount", amount);

        JSONObject newOrder = pciDssOrder(true);
        payload.put("order_id", newOrder.get("order_id"));

        BaseApiResponse response = orderApi.Capture(payload);
        assert response.isSuccess();

        JSONObject order = response.getParsedResponse();
        assert order.getString("capture_status").equals("captured");
    }

    @Test
    public void testOrderReverse() {
        JSONObject payload = new JSONObject();
        payload.put("currency", currency);
        payload.put("amount", amount);
        JSONObject newOrder = pciDssOrder(false);
        payload.put("order_id", newOrder.get("order_id"));

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

        JSONArray order = response.getReportList();
        assert order.length() == 1;
    }

    @Test
    public void testSubscriptionStop() {
        JSONObject payload = new JSONObject();
        payload.put("order_id", "test123");

        BaseApiResponse response = orderApi.SubscriptionStop(payload);
        assert response.isSuccess();

        JSONObject order = response.getResponse();
        assert order.get("status").equals("disabled");
    }

    @Test
    public void testReports() {
        JSONObject payload = new JSONObject();
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        String dateString = "20.01.2023";
        LocalDate currentDate = LocalDate.parse(dateString, dateFormatter);
        String currentDateStr = currentDate.format(dateFormatter);
        LocalDate dateMinusTenDays = currentDate.minusDays(1);
        String dateMinusTenDaysStr = dateMinusTenDays.format(dateFormatter);
        payload.put("date_to", currentDateStr);
        payload.put("date_from", dateMinusTenDaysStr);

        BaseApiResponse response = orderApi.Reports(payload);
        assert response.isSuccess();

        JSONArray orders = response.getReportList();
        assert orders.length() > 1;
    }
}
