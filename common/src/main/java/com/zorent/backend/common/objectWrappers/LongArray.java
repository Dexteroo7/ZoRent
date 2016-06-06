package com.zorent.backend.common.objectWrappers;

import com.google.common.primitives.Longs;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

/**
 * Created by dexter on 28/10/15.
 */
public final class LongArray implements Serializable {

    private static final long serialVersionUID = 8973683767349863L;

    public static LongArray of(long[] longs) {
        return new LongArray(longs);
    }

    private long[] longs;

    private LongArray(long[] longs) {
        this.longs = longs;
    }

    private LongArray() {

    }

    private void setLongs(long[] longs) {
        this.longs = longs;
    }

    public long[] getLongs() {
        return longs;
    }

    public List<Long> asList() {
        return Longs.asList(longs);
    }

    public Iterable<Long> asIterable() {
        return Longs.asList(longs);
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