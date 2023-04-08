package com.cloudipsp.cloudipspsdk;

import com.cloudipsp.cloudipspsdk.api.BaseConstants;

public class CloudIpspConfiguration {
    private Integer merchantId;
    private String secretKey;
    private String creditKey;
    private String version = "1.0.1";
    private int maxRetries = 3;
    protected String overrideApiURL;
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
    public CloudIpspConfiguration setMerchantId(final Integer merchantId) {
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
     * @param secretKey String
     * @return secretKey
     */
    public CloudIpspConfiguration setSecretKey(String secretKey) {
        this.secretKey = secretKey;
        return this;
    }

    /**
     * @return returns the private key object from the Configuration
     */
    public String getCreditKey() {
        return creditKey;
    }

    /**
     * @param creditKey String
     * @return secretKey
     */
    public CloudIpspConfiguration setCreditKey(String creditKey) {
        this.creditKey = creditKey;
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
    public CloudIpspConfiguration setMaxRetries(final int maxRetries) {
        this.maxRetries = maxRetries;
        return this;
    }

    /**
     * @return overrideApiURL Returns overridden MWS Service URL in Configuration
     */
    public String getOverrideApiURL() {
        return overrideApiURL;
    }

    /**
     * @return Returns clientConnections from Configuration
     */
    public Integer getClientConnections() {
        if (clientConnections != null && clientConnections != 0) {
            return clientConnections;
        } else {
            return BaseConstants.MAX_CLIENT_CONNECTIONS;
        }
    }

    /**
     * @param clientConnections Sets the maximum number of Client Connections to be made
     * @return the Configuration object
     */
    public CloudIpspConfiguration setClientConnections(int clientConnections) {
        this.clientConnections = clientConnections;
        return this;
    }

    /**
     *
     * @return version
     */
    public String getVersion(){
        return this.version;
    }

    /**
     * @param version of request
     */
    public void setVersion(String version){
        this.version = version;
    }

}
