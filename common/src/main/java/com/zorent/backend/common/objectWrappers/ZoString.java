package com.zorent.backend.common.objectWrappers;

import java.io.Serializable;

/**
 * Created by dexter on 28/12/14.
 */
public class ZoString implements Serializable {

    private static final long serialVersionUID = 897368376364536864L;

    public static final ZoString TRUE = new ZoString("true");
    public static final ZoString FALSE = new ZoString("false");

    private String string;

    public ZoString(String string) {
        this.string = string;
    }

    public ZoString() {
    }

    public String getString() {
        return string;
    }

    public void setString(String string) {
        this.string = string;
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