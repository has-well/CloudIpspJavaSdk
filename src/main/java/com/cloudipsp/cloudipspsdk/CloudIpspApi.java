package com.cloudipsp.cloudipspsdk;

import com.cloudipsp.cloudipspsdk.api.PaymentApi;
import com.cloudipsp.cloudipspsdk.exceptions.CloudIpspException;

public class CloudIpspApi implements CloudIpspSdk {
    private final Configuration configuration;
    private PaymentApi paymentApi;

    /**
     *
     * @param configuration Base configuration
     * @throws CloudIpspException
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
    }

    @Override
    public PaymentApi getPaymentApi() {
        return this.paymentApi;
    }

    @Override
    public String getSdkVersion() {
        return "1.0.0";
    }
}
