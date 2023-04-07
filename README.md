<p align="center">
  <img width="200" height="200" src="https://avatars0.githubusercontent.com/u/15383021?s=200&v=4">
</p>

# CloudIpspSdk[Fondy]
## Payment service provider
A payment service provider (PSP) offers shops online services for accepting electronic payments by a variety of payment methods including credit card, bank-based payments such as direct debit, bank transfer, and real-time bank transfer based on online banking. Typically, they use a software as a service model and form a single payment gateway for their clients (merchants) to multiple payment methods.
[read more](https://en.wikipedia.org/wiki/Payment_service_provider)

Requirements
------------
- [Fondy account](https://portal.fondy.eu)
- Java 8


## SDK Installation

If Maven project, add sdk dependency in <dependencies> section:

```xml
<dependencies>
    <dependency>
        <groupId>com.cloudipsp.cloudipspsdk</groupId>
        <artifactId>java-cloudipsp-sdk-client</artifactId>
        <version>1.0.0</version>
    </dependency>
</dependencies>
```

## Simple start
```java
    final Configuration config = new Configuration()
                                        .setSecretKey("test")
                                        .setMerchantId(1396424);
    client = new CloudIpspApi(config);
    pamentApi = client.getPaymentApi(); // Get payment api instance
    orderApi = client.getOrderApi(); // Get api instance for working with orders
    /**
     * Generate payment request
     */
    JSONObject payload = new JSONObject();
    payload.put("currency", "EUR");
    payload.put("amount", 100);
    payload.put("order_id", "test123");
    BaseApiResponse response = pamentApi.paymentUrl(payload);
    JSONObject order = response.getParsedResponse();
    URI checkout_url = response.getCheckoutUrl();
    /**
     * Generate capture request
     */
    JSONObject payload = new JSONObject();
    payload.put("currency", "EUR");
    payload.put("amount", 100);
    payload.put("order_id", "test123");
    BaseApiResponse response = orderApi.Capture(payload);
    JSONObject order = response.getParsedResponse();
```