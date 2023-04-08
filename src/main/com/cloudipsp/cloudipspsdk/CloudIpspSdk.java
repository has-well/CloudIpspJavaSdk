package com.cloudipsp.cloudipspsdk;

import com.cloudipsp.cloudipspsdk.api.OrderApi;
import com.cloudipsp.cloudipspsdk.api.PaymentApi;
import com.cloudipsp.cloudipspsdk.api.PciDss;

/**
 * Basic Interface
 */
public interface CloudIpspSdk {
    PaymentApi getPaymentApi();

    OrderApi getOrderApi();

    PciDss getPciDssApi();

}
