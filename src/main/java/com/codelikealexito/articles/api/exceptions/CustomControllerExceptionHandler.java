package com.codelikealexito.articles.api.exceptions;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@Slf4j
@ControllerAdvice
public class CustomControllerExceptionHandler {

    // exception handlers

    @ExceptionHandler({HttpClientErrorException.Unauthorized.class})
    public ResponseEntity<ExceptionBody> handleUnauthorizedExceptions(Exception e) {
        return new ResponseEntity<>(new ExceptionBody(e, "Default message"),
                HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler({IllegalArgumentException.class, IllegalStateException.class, MethodArgumentNotValidException.class,
            HttpMessageNotReadableException.class, ServletRequestBindingException.class, NumberFormatException.class,
            MethodArgumentTypeMismatchException.class, MissingServletRequestParameterException.class})
    public ResponseEntity<ExceptionBody> handleBadRequestExceptions(Exception e) {
        return new ResponseEntity<>(new ExceptionBody(e, "Default message"),
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(CustomResponseStatusException.class)
    public ResponseEntity<ExceptionBody> handleResponseStatusException(CustomResponseStatusException ex){
        return new ResponseEntity<>(new ExceptionBody(ex.getErrorCode(), "Default message"), HttpStatus.valueOf(ex.getRawStatusCode()));
    }

    public static class ExceptionBody {
        private String errorCode;
        private String reason;

        public ExceptionBody() {
        }

        public ExceptionBody(Throwable e, String reason) {
            super();
            this.errorCode = ErrorCodes.getErrorCode(e.getClass().getSimpleName());
            this.reason = reason;
        }

        public ExceptionBody(String errorCode, String reason) {
            this.errorCode = errorCode;
            this.reason = reason;
        }

        @Override
        public String toString() {
            return "ExceptionBody{" +
                    "errorCode" + errorCode + '\'' +
                    ", reason='" + reason + '\'' +
                    '}';
        }
    }

    public enum ErrorCodes {
        DEFAULT_EXCEPTION_CODE("ERR500");

        private final String errorCode;

        ErrorCodes(String errorCode) {
            this.errorCode = errorCode;
        }

        public static String getErrorCode(String code) {
            try {
                return valueOf(code).errorCode;
            } catch (IllegalArgumentException e) {
                return DEFAULT_EXCEPTION_CODE.errorCode;
            }
        }

        public static String getDefaultErrorCode() {
            return DEFAULT_EXCEPTION_CODE.errorCode;
        }
    }
}
