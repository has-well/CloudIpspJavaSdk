package com.cloudipsp.cloudipspsdk;

import com.cloudipsp.cloudipspsdk.api.BaseConstants;

public class Configuration {
    private Integer merchantId;
    private String secretKey;
    private int maxRetries = 3;
    protected String overrideServiceURL;
    private Integer clientConnections;

    /**
     * @return returns the public key id from the Configuration
     */
    public Integer getMerchantId() {
        return merchantId;
    }

    /**
     * @param merchantId The public key id of the merchant
     * @return the Configuration object
     */
    public Configuration setMerchantId(final Integer merchantId) {
        this.merchantId = merchantId;
        return this;
    }

    /**
     * @return returns the private key object from the Configuration
     */
    public String getSecretKey() {
        return secretKey;
    }

    /**
     *
     * @param secretKey
     * @return secretKey
     */
    public Configuration setSecretKey(String secretKey) {
        this.secretKey = secretKey;
        return this;
    }

    /**
     * @return the maximum number of retries to be made
     */
    public int getMaxRetries() {
        return maxRetries;
    }

    /**
     * @param maxRetries Sets the maximum number of retries to be made in case of internal server
     *                   errors or throttling errors, in Configuration
     * @return the Configuration object
     */
    public Configuration setMaxRetries(final int maxRetries) {
        this.maxRetries = maxRetries;
        return this;
    }

    /**
     * @return overrideServiceURL Returns overridden MWS Service URL in Configuration
     */
    public String getOverrideServiceURL() {
        return overrideServiceURL;
    }

    /**
     * @return Returns clientConnections from Configuration
     */
    public Integer getClientConnections() {
        if(clientConnections != null && clientConnections != 0) {
            return clientConnections;
        } else {
            return BaseConstants.MAX_CLIENT_CONNECTIONS;
        }
    }

    /**
     * @param clientConnections Sets the maximum number of Client Connections to be made
     * @return the Configuration object
     */
    public Configuration setClientConnections(int clientConnections) {
        this.clientConnections = clientConnections;
        return this;
    }

}
