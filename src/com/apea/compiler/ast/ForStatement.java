package com.apea.compiler.ast;

public class ForStatement implements Statement {

    public final Var var;
    public final IntegerConst left;
    public final IntegerConst right;
    public final Statement body;

    public ForStatement(Var var,IntegerConst left, IntegerConst right, Statement body) {
        this.var = var;
        this.left = left;
        this.right = right;
        this.body = body;
    }

    public void accept(AstVisitor v) {
        v.visit(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ForStatement)) return false;

        ForStatement that = (ForStatement) o;

        if (!var.equals(that.var)) return false;
        if (!left.equals(that.left)) return false;
        if (!right.equals(that.right)) return false;
        return body.equals(that.body);

    }

    @Override
    public String toString() {
        return "for " +var+" := "+ left +" to "+ right + " do " + body;
    }
}
