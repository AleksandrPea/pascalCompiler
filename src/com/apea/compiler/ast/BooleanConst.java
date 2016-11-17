package com.apea.compiler.ast;

public class BooleanConst implements Expr {
    public final boolean value;

    public BooleanConst(boolean value) {
        this.value = value;
    }

    public void accept(AstVisitor v) {
        v.visit(this);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof BooleanConst) {
            return this.value == ((BooleanConst)obj).value;
        } else {
            return false;
        }
    }

    @Override
    public String toString() {
        return ""+value;
    }
}
