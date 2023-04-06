package com.cloudipsp.cloudipspsdk;

import com.cloudipsp.cloudipspsdk.api.BaseConstants;
import com.cloudipsp.cloudipspsdk.exceptions.CloudipspException;
import org.json.JSONObject;
import org.apache.commons.codec.digest.DigestUtils;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.StringJoiner;
import java.util.Iterator;

public class Utils {
    /**
     *
     * @return URI
     */
    public static URI getServiceURI(Configuration configuration, String path) throws CloudipspException {
        URI uri;
        try {
            String endpoint = BaseConstants.endpoint;
            if (configuration.getOverrideApiURL() != null) {
                endpoint = "https://" + configuration.getOverrideApiURL();
            } else {
                endpoint = "https://" + endpoint;
            }
            uri = new URI(endpoint + "/api" + path);
        } catch (URISyntaxException e) {
            throw new CloudipspException(e.getMessage(), null, null);
        }
        return uri;
    }

    /**
     *
     * @return HashMap headers
     */
    public static Map<String, String> getDefaultHeaders() {
        Map<String, String> headers = new HashMap<>();
        headers.put("Accept", "application/json");
        headers.put("Content-Type", "application/json; charset=UTF-8");
        headers.put("User-Agent", BaseConstants.sdkHeader);
        return headers;
    }

    /**
     * get random uuid
     * @return string
     */
    public static String generateOrderID(){
        return UUID.randomUUID().toString();
    }

    /**
     * get order desc
     * @return string
     */
    public static String generateOrderDesc(String orderID){
        return "Pay for order: " + orderID;
    }

    /**
     * get signature v1
     * @return string
     */
    public static String generateSignature(JSONObject requset, String primaryKey){
        StringJoiner signature = new StringJoiner(BaseConstants.DELIMITER);
        signature.add(primaryKey);
        Iterator<String> keys = requset.keys();

        while (keys.hasNext()) {
            String key = keys.next();
            String value = requset.get(key).toString();
            if (!value.isEmpty()){
                signature.add(value);
            }
        }
        return DigestUtils.sha1Hex(signature.toString());
    }
}
