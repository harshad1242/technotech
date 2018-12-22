package com.technotech.technotechapplication.util;

/**
 * Created by welcome on 29-11-2016.
 */

public final class Singleton {
    private static final Singleton INSTANCE = new Singleton();
    private Singleton() {}

    public static String getDynamicrssi() {
        return dynamicrssi;
    }

    public static void setDynamicrssi(String dynamicrssinew) {
        dynamicrssi = dynamicrssinew;
    }

    public static String dynamicrssi;

    public static Singleton getInstance() {
        return INSTANCE;
    }
}
