package com.apea.compiler.types;

public class StringType implements Type {
    public static StringType instance = new StringType();

    private StringType() {}


    @Override
    public String toString() {
        return "string";
    }
}
