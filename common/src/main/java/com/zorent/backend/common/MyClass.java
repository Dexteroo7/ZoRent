package com.zorent.backend.common;

public class MyClass {

    public static void main(String[] args) {

        for (Tags tag : Tags.values())
            System.out.println(tag.toString());
    }
}