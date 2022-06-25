package com.codelikealexito.articles.api.exceptions;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

@Getter
public class CustomResponseStatusException extends ResponseStatusException {

    private HttpStatus status;
    private String errorCode;
    private String reason;
    private String message;
    private String trackingId;

    public CustomResponseStatusException(HttpStatus status, String errorCode, String reason) {
        super(status);
        this.status = status;
        this.errorCode = errorCode;
        this.reason = reason;
    }

    public CustomResponseStatusException(HttpStatus status, String errorCode, String reason, String message, String trackingId) {
        super(status);
        this.status = status;
        this.errorCode = errorCode;
        this.reason = reason;
        this.message = message;
        this.trackingId = trackingId;
    }

    public CustomResponseStatusException(HttpStatus status, Throwable e) {
        super(status);
        this.status = status;
        this.errorCode = CustomControllerExceptionHandler.ErrorCodes.getErrorCode(e.getClass().getSimpleName());
        this.reason = status.getReasonPhrase();
        StringBuilder buffer = new StringBuilder(128).append(e.getMessage());
        while(e.getCause() != null) {
            e = e.getCause();
            buffer.append(" - ").append(e.getMessage());
        }
        if(!buffer.toString().equals("null")){
            this.message = buffer.toString();
        }
    }

    @Override
    public String toString() {
        return "CustomResponseStatusException{" +
                "status=" + status +
                ", errorCode='" + errorCode + '\'' +
                ", reason='" + reason + '\'' +
                ", message='" + message + '\'' +
                ", trackingId='" + trackingId + '\'' +
                '}';
    }
}
