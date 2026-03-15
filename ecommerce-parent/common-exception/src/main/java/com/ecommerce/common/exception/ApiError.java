package com.ecommerce.common.exception;

import java.time.LocalDateTime;

public class ApiError {

    private int status;
    private String message;
    private String errorCode;
    private String correlationId;
    private LocalDateTime timestamp;

    public ApiError(int status,
                    String message,
                    String errorCode,
                    String correlationId,
                    LocalDateTime timestamp) {

        this.status = status;
        this.message = message;
        this.errorCode = errorCode;
        this.correlationId = correlationId;
        this.timestamp = timestamp;
    }

    public int getStatus() { return status; }

    public String getMessage() { return message; }

    public String getErrorCode() { return errorCode; }

    public String getCorrelationId() { return correlationId; }

    public LocalDateTime getTimestamp() { return timestamp; }
}