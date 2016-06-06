package com.zorent.backend.common.objectWrappers;

import com.google.common.collect.Iterables;

import java.util.Arrays;
import java.util.List;

/**
 * If use array or list around string it passes it in URL
 * Created by dexter on 03/06/2016.
 */
public class StringArray {

    private static final long serialVersionUID = 8976837673463563L;

    public static StringArray of(String[] strings) {
        return new StringArray(strings);
    }

    private String[] strings;

    private void setStrings(String[] strings) {
        this.strings = strings;
    }

    private StringArray(String[] strings) {

    }

    private StringArray() {

    }

    public String[] getStrings() {
        return strings;
    }

    public List<String> asList() {

        return Arrays.asList(strings);
    }

    public Iterable<String> asIterable() {
        return Iterables.cycle(strings);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        StringArray that = (StringArray) o;

        // Probably incorrect - comparing Object[] arrays with Arrays.equals
        return Arrays.equals(strings, that.strings);

    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(strings);
    }

    @Override
    public String toString() {
        return "StringArray{" +
                "strings=" + Arrays.toString(strings) +
                '}';
    }
}
