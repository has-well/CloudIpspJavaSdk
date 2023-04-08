package com.cloudipsp.cloudipspsdk;

import com.cloudipsp.cloudipspsdk.api.BaseConstants;
import com.cloudipsp.cloudipspsdk.exceptions.CloudIpspException;
import org.json.JSONObject;
import org.apache.commons.codec.digest.DigestUtils;
import java.util.Base64;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.StringJoiner;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Utils {
    /**
     *
     * @return URI
     */
    public static URI getServiceURI(Configuration configuration, String path) throws CloudIpspException {
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
            throw new CloudIpspException(e.getMessage(), null, null);
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
    public static String generateSignature(JSONObject request, String primaryKey){
        List<Map.Entry<String, Object>> sorted_request = sortData(request);
        StringJoiner signature = new StringJoiner(BaseConstants.DELIMITER);
        signature.add(primaryKey);

        sorted_request.forEach(e -> {
            if (!e.getValue().toString().isEmpty()) {
                signature.add(e.getValue().toString());
            }
        });
        return DigestUtils.sha1Hex(signature.toString());
    }

    /**
     * signatrue V2
     * @param request json
     * @param primaryKey string
     * @return sha1 string
     */
    public static String generateSignatureV2(String request, String primaryKey){
        StringJoiner signature = new StringJoiner(BaseConstants.DELIMITER);
        signature.add(primaryKey);
        signature.add(request);
        return DigestUtils.sha1Hex(signature.toString());
    }

    /**
     *
     * @param jsonObject init
     * @return sorted object
     */
    public static List<Map.Entry<String, Object>> sortData(JSONObject jsonObject) {
        List<Map.Entry<String, Object>> entries = new ArrayList<>(jsonObject.toMap().entrySet());

        entries.sort(Map.Entry.comparingByKey());

        return entries;
    }

    public static String toBase64(String data) {
        return Base64.getEncoder().encodeToString(data.getBytes());
    }

    public static String fromBase64(String data) {
        return new String(Base64.getDecoder().decode(data.getBytes()));
    }
}
