package com.apea.compiler.ast;

public class ArrayVar extends Var {
    public final Expr offset;

    public ArrayVar(String name, Expr offset) {
        super(name);
        this.offset = offset;
    }

    @Override
    public boolean equals(Object o) {
        return super.equals(o) && offset.equals(((ArrayVar)o).offset);

    }

    @Override
    public String toString() {
        return name +"["+offset+"]";
    }
}
