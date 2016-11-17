package com.apea.compiler.types;

public class BooleanType implements Type {
    public static BooleanType instance = new BooleanType();

    private BooleanType() {

    }

    @Override
    public String toString() {
        return "boolean";
    }
}
