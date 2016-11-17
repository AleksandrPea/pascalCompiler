package com.apea.compiler.types;

public class NoneType implements Type {
    public static NoneType instance = new NoneType();
    
    private NoneType() {
    }

    @Override
    public String toString() {
        return "none";
    }
}
