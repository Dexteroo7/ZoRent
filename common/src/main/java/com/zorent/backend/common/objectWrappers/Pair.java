package com.zorent.backend.common.objectWrappers;

import java.io.Serializable;

/**
 * Created by dexter on 02/02/16.
 * Never change this class
 */

public final class Pair<A, B> implements Serializable {

    private static final long serialVersionUID = 5335664764756L;

    public A fst;
    public B snd;

    private Pair(A var1, B var2) {
        this.fst = var1;
        this.snd = var2;
    }

    public Pair() {
    }

    public A getFst() {
        return fst;
    }

    public B getSnd() {
        return snd;
    }

    public Pair<A, B> of(A a, B b) {

        return new Pair<>(a, b);
    }

    public final String toString() {
        return "Pair[" + this.fst + "," + this.snd + "]";
    }
}