package com.apea.compiler.types;

public class FloatType implements Type {
    public static FloatType instance = new FloatType();

    private FloatType() {}


    @Override
    public String toString() {
        return "float";
    }
}
