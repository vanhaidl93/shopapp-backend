package com.hainguyen.shop.utils;

public final class Constants {

    // no one can extend and override the values present inside these constants class.
    private Constants() {
        // restrict instantiation
    }

    public static final String LOGIN_SUCCESS = "LOGIN_SUCCESS";
    public static final String STATUS_201 = "201";
    public static final String MESSAGE_201 = "MESSAGE_201";
    public static final String STATUS_200 = "200";
    public static final String STATUS_417 = "417";
    public static final String MESSAGE_417_UPDATE = "MESSAGE_417_UPDATE";
    public static final String MESSAGE_417_DELETE = "MESSAGE_417_DELETE";
    public static final String MESSAGE_200 = "MESSAGE_200";
    public static final String STATUS_500 = "500";
    public static final String MESSAGE_500 = "An error occurred. Please try again or contact Dev team";

    // JWT TOKEN
    public static final String JWT_HEADER = "Authorization";

}
