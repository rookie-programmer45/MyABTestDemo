package com.ljc.common.enums;

public enum RoutingVersionEnum {

    A ("A"),
    B ("B");

    private final String value;

    RoutingVersionEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public boolean equals(String s) {
        return this.getValue().equals(s);
    }
}
