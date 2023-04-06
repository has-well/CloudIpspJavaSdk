package com.cloudipsp.cloudipspsdk.exceptions;

import java.io.IOException;

public class CloudipspException extends RuntimeException {
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
    public CloudipspException(String message, String code, String request_id) {
        this.message = message;
        this.error_code = code;
        this.request_id = request_id;
    }

    /**
     * base Exception
     */
    public CloudipspException(IOException ioException) {
        this.message = ioException.getMessage();
        this.error_code = null;
        this.request_id = null;
    }
}
