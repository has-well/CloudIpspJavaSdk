package com.cloudipsp.cloudipspsdk;

import com.cloudipsp.cloudipspsdk.api.OrderApi;
import com.cloudipsp.cloudipspsdk.api.PaymentApi;
import com.cloudipsp.cloudipspsdk.exceptions.CloudIpspException;

public class CloudIpspApi implements CloudIpspSdk {
    private final Configuration configuration;
    private PaymentApi paymentApi;
    private OrderApi orderApi;

    /**
     *
     * @param configuration Base configuration
     * @throws CloudIpspException basic
     */
    public CloudIpspApi(Configuration configuration) throws CloudIpspException {
        this.configuration = configuration;
        loadApis();
    }

    /**
     * Load all api methods
     */
    private void loadApis() {
        this.paymentApi = new PaymentApi(this.configuration);
        this.orderApi = new OrderApi(this.configuration);
    }

    @Override
    public PaymentApi getPaymentApi() {
        return this.paymentApi;
    }

    @Override
    public OrderApi getOrderApi() {
        return this.orderApi;
    }

    @Override
    public String getSdkVersion() {
        return "1.0.0";
    }
}
