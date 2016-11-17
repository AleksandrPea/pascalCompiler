package com.apea.compiler.ast;

public class Var implements Expr {
    public final String name;

    public Var(String name) {
        this.name = name;
    }

    public void accept(AstVisitor v) {
        v.visit(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Var)) return false;

        Var var = (Var) o;

        return name.equals(var.name);

    }

    @Override
    public String toString() {
        return name;
    }
}
