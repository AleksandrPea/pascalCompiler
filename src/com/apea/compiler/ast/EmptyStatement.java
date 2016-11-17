package com.apea.compiler.ast;

public class EmptyStatement implements Statement {

    @Override
    public void accept(AstVisitor v) {v.visit(this);}

    @Override
    public boolean equals(Object obj) {
        return (obj instanceof EmptyStatement);
    }

    @Override
    public String toString() {
        return "{}";
    }
}
