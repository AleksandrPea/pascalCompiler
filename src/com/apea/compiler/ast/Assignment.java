package com.apea.compiler.ast;

public class Assignment implements Statement {
    public final Var var;
    public final Expr expr;

    public Assignment(Var var, Expr expr) {
        this.var = var;
        this.expr = expr;
    }

    public void accept(AstVisitor v) {
        v.visit(this);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Assignment) {
            Assignment that = (Assignment)obj;
            return
                    this.var.equals(that.var) &&
                            this.expr.equals(that.expr);
        } else {
            return false;
        }
    }

    @Override
    public String toString() {
        return var + " := " + expr;
    }
}
