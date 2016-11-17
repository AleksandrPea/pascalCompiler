package com.apea.compiler.types;

public class IntegerType implements Type {
    public static IntegerType instance = new IntegerType();

    private IntegerType() {}

    @Override
    public String toString() {
        return "integer";
    }
}
