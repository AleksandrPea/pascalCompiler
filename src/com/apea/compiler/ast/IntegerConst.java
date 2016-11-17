package com.apea.compiler.ast;

public class IntegerConst implements Expr {
    public final int value;

    public IntegerConst(int value) {
        this.value = value;
    }

    public void accept(AstVisitor v) {
        v.visit(this);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof IntegerConst) {
            return this.value == ((IntegerConst)obj).value;
        } else {
            return false;
        }
    }

    @Override
    public String toString() {
        return ""+value;
    }
}
