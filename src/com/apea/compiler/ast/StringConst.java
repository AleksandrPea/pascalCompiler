package com.apea.compiler.ast;

public class StringConst implements Expr {
    public final String value;

    public StringConst(String value) {
        this.value = value;
    }

    public void accept(AstVisitor v) {
        v.visit(this);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof StringConst) {
            return this.value == ((StringConst)obj).value;
        } else {
            return false;
        }
    }

    @Override
    public String toString() {
        return "\""+value+"\"";
    }
}
