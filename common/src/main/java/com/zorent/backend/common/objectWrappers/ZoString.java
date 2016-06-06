package com.zorent.backend.common.objectWrappers;

import java.io.Serializable;

/**
 * Created by dexter on 28/12/14.
 */
public class ZoString implements Serializable {

    private static final long serialVersionUID = 897368376364536864L;

    public static final ZoString TRUE = new ZoString("true");
    public static final ZoString FALSE = new ZoString("false");

    private final String string;

    public static ZoString of(String string) {
        return new ZoString(string);
    }

    public static ZoString of(Object string) {
        return new ZoString(string + "");
    }

    private ZoString(String string) {
        this.string = string;
    }

    private ZoString() {
        throw new IllegalAccessError("Can't access");
    }

    public String getString() {
        return string;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ZoString)) return false;

        ZoString zoString = (ZoString) o;

        return string != null ? string.equals(zoString.string) : zoString.string == null;

    }

    @Override
    public int hashCode() {
        return string != null ? string.hashCode() : 0;
    }
}