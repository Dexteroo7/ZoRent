package com.zorent.backend.common.objectWrappers;

import java.io.Serializable;
import java.util.List;

/**
 * Created by dexter on 30/12/15.
 */
public final class LongList implements Serializable {

    private static final long serialVersionUID = 8973683767349864L;

    public List<Long> longs;

    public LongList(List<Long> longs) {
        this.longs = longs;
    }

    public LongList() {

    }

    public List<Long> getLongs() {
        return longs;
    }

    public void setLongs(List<Long> longs) {
        this.longs = longs;
    }

    @Override
    public boolean equals(Object o) {

        if (this == o) return true;
        if (!(o instanceof LongList)) return false;

        LongList longList = (LongList) o;

        return longs != null ? longs.equals(longList.longs) : longList.longs == null;

    }

    @Override
    public int hashCode() {

        return longs != null ? longs.hashCode() : 0;
    }
}
