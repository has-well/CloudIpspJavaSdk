package com.cloudipsp.cloudipspsdk;

import com.cloudipsp.cloudipspsdk.api.BaseApiResponse;
import com.cloudipsp.cloudipspsdk.api.PaymentApi;
import com.cloudipsp.cloudipspsdk.api.PciDss;
import com.cloudipsp.cloudipspsdk.exceptions.CloudIpspException;
import org.json.JSONArray;
import org.junit.Before;
import org.junit.Test;
import org.json.JSONObject;

import java.net.URI;
import java.util.Objects;

public class PaymentTest {
    private CloudIpspApi client;
    private Configuration config;
    private PaymentApi pamentApi;
    private PciDss pciDssApi;

    private final Integer amount = 100;
    private final String currency = "UAH";
    private final String without3dsCard = "4444555511116666";
    private final String with3dsCard = "4444555566661111";

    @Before
    public void setUp() {
        // Load private key and configure client
        config = new Configuration()
                .setSecretKey("test")
                .setCreditKey("testcredit")
                .setMerchantId(1396424);
        client = new CloudIpspApi(config);
        pamentApi = client.getPaymentApi();
        pciDssApi = client.getPciDssApi();
    }

    @Test
    public void testConfiguration() {
        assert Objects.equals(config.getSecretKey(), "test");
        assert Objects.equals(config.getMerchantId(), 1396424);
        assert Objects.equals(config.getMaxRetries(), 3);
        assert Objects.equals(config.getVersion(), "1.0.1");
    }

    @Test
    public void testApiCallFail() {
        JSONObject payload = new JSONObject();
        payload.put("amount", 1.10);
        payload.put("currency", "III");
        try {
            pamentApi.paymentUrl(payload);
        } catch (final CloudIpspException e) {
            assert Objects.equals(e.error_code, "1007");
            assert Objects.equals(e.message, "Parameter `currency` is incorrect");
        }
    }

    @Test
    public void testApiCallSuccess() {
        JSONObject payload = new JSONObject();
        payload.put("amount", amount);
        payload.put("currency", currency);

        BaseApiResponse response = pamentApi.paymentUrl(payload);
        assert response.isSuccess();

        JSONObject order = response.getParsedResponse();
        assert order.has("checkout_url");
    }

    @Test
    public void testApiCallGetURL() {
        JSONObject payload = new JSONObject();
        payload.put("amount", amount);
        payload.put("currency", currency);

        BaseApiResponse response = pamentApi.paymentUrl(payload);
        assert response.isSuccess();

        URI checkoutUrl = response.getCheckoutUrl();
        assert !checkoutUrl.toString().isEmpty();
    }

    @Test
    public void testApiCallVerification() {
        JSONObject payload = new JSONObject();
        payload.put("currency", currency);

        BaseApiResponse response = pamentApi.paymentVerificationUrl(payload);
        assert response.isSuccess();

        JSONObject order = response.getParsedResponse();
        assert order.has("checkout_url");
    }

    @Test
    public void testApiCallToken() {
        JSONObject payload = new JSONObject();
        payload.put("currency", currency);
        payload.put("amount", amount);

        BaseApiResponse response = pamentApi.paymentToken(payload);
        assert response.isSuccess();

        JSONObject order = response.getParsedResponse();
        assert order.has("token");
    }

    @Test
    public void testApiCallSubscriptions() {
        JSONObject payload = new JSONObject();
        payload.put("currency", currency);
        payload.put("amount", amount);

        BaseApiResponse response = pamentApi.paymentSubscriptions(payload);
        assert response.isSuccess();

        JSONObject order = response.getParsedResponse();
        assert order.has("checkout_url");
    }

    @Test
    public void testApiCallSplit() {
        JSONObject payload = new JSONObject();
        JSONArray receivers = new JSONArray();
        JSONObject receiver = new JSONObject();
        JSONObject requisites = new JSONObject();

        requisites.put("amount", amount);
        requisites.put("settlement_description", "Назначение платежа для банковского перевода");
        requisites.put("merchant_id", 1396424);
        receiver.put("requisites", requisites);
        receiver.put("type", "merchant");
        receivers.put(receiver);
        payload.put("receiver", receivers);
        payload.put("currency", "UAH");
        payload.put("amount", amount);
        payload.put("operation_id", Utils.generateOrderID());
        payload.put("order_type", "settlement");

        BaseApiResponse response = pamentApi.paymentSplit(payload);
        assert response.isSuccess();

        JSONObject order = response.getParsedResponse();

        assert order.has("order_status");
        assert order.get("order_status").equals("created");
    }

    @Test
    public void testP2pCredit() {
        JSONObject payload = new JSONObject();
        payload.put("currency", "UAH");
        payload.put("amount", amount);
        payload.put("receiver_card_number", "4444555566661111");

        BaseApiResponse response = pciDssApi.p2pCredit(payload);
        assert response.isSuccess();

        JSONObject order = response.getParsedResponse();
        assert order.has("order_status");
        assert order.get("order_status").equals("approved");
    }

    public JSONObject pciDssOrder(boolean is3d) {
        JSONObject payload = new JSONObject();
        payload.put("currency", "UAH");
        payload.put("amount", amount);
        payload.put("card_number", is3d ? with3dsCard : without3dsCard);
        payload.put("cvv2", "123");
        payload.put("expiry_date", "1244");
        payload.put("required_rectoken", "Y");

        BaseApiResponse response = pciDssApi.stepOne(payload);
        assert response.isSuccess();

        return response.getParsedResponse();
    }

    @Test
    public void testPciDssStepOne() {
        JSONObject order = pciDssOrder(false);
        assert order.has("order_status");
        assert order.get("order_status").equals("approved");
    }

    @Test
    public void testPciDssStepTwo() {
        JSONObject order = pciDssOrder(true);
        assert order.get("response_status").equals("success");
    }

    @Test
    public void testApiCallRecurring() {
        JSONObject payload = new JSONObject();
        payload.put("currency", currency);
        payload.put("amount", amount);
        JSONObject order = pciDssOrder(false);
        payload.put("rectoken", order.get("rectoken"));

        BaseApiResponse response = pamentApi.paymentRecurrig(payload);
        assert response.isSuccess();

        JSONObject orderRecurring = response.getParsedResponse();
        assert orderRecurring.get("order_status").equals("approved");
    }

}
