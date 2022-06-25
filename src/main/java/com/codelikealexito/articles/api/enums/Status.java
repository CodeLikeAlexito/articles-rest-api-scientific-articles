package com.codelikealexito.articles.api.enums;

public enum Status {
    APPROVED("STATUS_APPROVED"),
    PENDING("STATUS_PENDING"),
    REJECTED("STATUS_REJECTED");

    private final String value;

    Status(String value){
        this.value = value;
    }
}
