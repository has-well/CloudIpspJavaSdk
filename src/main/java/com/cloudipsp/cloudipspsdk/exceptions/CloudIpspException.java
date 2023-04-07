package com.cloudipsp.cloudipspsdk.exceptions;

import java.io.IOException;

public class CloudIpspException extends RuntimeException {
    /**
     * error message
     */
    public final String message;
    /**
     * error code
     */
    public final String error_code;
    /**
     * request_id
     */
    public final String request_id;

    /**
     * @param message An error message
     * @param code    An error code
     */
    public CloudIpspException(String message, String code, String request_id) {
        this.message = message;
        this.error_code = code;
        this.request_id = request_id;
    }

    /**
     * base Exception
     */
    public CloudIpspException(IOException ioException) {
        this.message = ioException.getMessage();
        this.error_code = null;
        this.request_id = null;
    }
    @Override
    public String toString() {
        return "CloudIpspsError[" +
                "message='" + message + '\'' +
                ", error_code=" + error_code +
                ", request_id='" + request_id + '\'' +
                ']';
    }

}
