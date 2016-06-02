package com.zorent.backend.common.objectWrappers;

import java.io.Serializable;
import java.util.Arrays;

/**
 * Created by dexter on 28/10/15.
 */
public final class LongArray implements Serializable {

    private static final long serialVersionUID = 8973683767349863L;

    public long[] longs;

    public LongArray(long[] longs) {
        this.longs = longs;
    }

    public LongArray() {
    }

    public long[] getLongs() {
        return longs;
    }

    public void setLongs(long[] longs) {
        this.longs = longs;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        LongArray longArray = (LongArray) o;

        return Arrays.equals(longs, longArray.longs);

    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(longs);
    }

    @Override
    public String toString() {
        return "LongArray{" +
                "longs=" + Arrays.toString(longs) +
                '}';
    }
}