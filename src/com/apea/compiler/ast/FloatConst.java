package com.apea.compiler.ast;

public class FloatConst implements Expr {
    public final double value;

    public FloatConst(double value) {
        this.value = value;
    }

    public void accept(AstVisitor v) {
        v.visit(this);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof FloatConst) {
            return this.value == ((FloatConst)obj).value;
        } else {
            return false;
        }
    }

    @Override
    public String toString() {
        return ""+value;
    }
}
