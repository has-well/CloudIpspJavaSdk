package com.cloudipsp.cloudipspsdk;

import com.cloudipsp.cloudipspsdk.api.OrderApi;
import com.cloudipsp.cloudipspsdk.api.PaymentApi;
import com.cloudipsp.cloudipspsdk.api.PciDss;
import com.cloudipsp.cloudipspsdk.exceptions.CloudIpspException;

public class CloudIpspApi implements CloudIpspSdk {
    private final CloudIpspConfiguration configuration;
    private PaymentApi paymentApi;
    private OrderApi orderApi;
    private PciDss pciDssApi;

    /**
     *
     * @param configuration Base configuration
     * @throws CloudIpspException basic
     */
    public CloudIpspApi(CloudIpspConfiguration configuration) throws CloudIpspException {
        this.configuration = configuration;
        loadApis();
    }

    /**
     * Load all api methods
     */
    private void loadApis() {
        this.paymentApi = new PaymentApi(this.configuration);
        this.orderApi = new OrderApi(this.configuration);
        this.pciDssApi = new PciDss(this.configuration);
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
    public PciDss getPciDssApi() {
        return this.pciDssApi;
    }
}
