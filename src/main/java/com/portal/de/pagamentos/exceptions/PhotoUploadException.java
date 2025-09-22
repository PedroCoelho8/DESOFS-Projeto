package com.portal.de.pagamentos.exceptions;

import java.io.IOException;

public class PhotoUploadException extends IOException {
    public PhotoUploadException(String message, Throwable cause) {
        super(message, cause);
    }
}
