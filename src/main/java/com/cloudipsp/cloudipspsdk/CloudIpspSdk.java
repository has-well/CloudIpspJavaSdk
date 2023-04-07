package com.cloudipsp.cloudipspsdk;

import com.cloudipsp.cloudipspsdk.api.PaymentApi;

/**
 * Basic Interface
 */
public interface CloudIpspSdk {
    String getSdkVersion();
    PaymentApi getPaymentApi();

}
