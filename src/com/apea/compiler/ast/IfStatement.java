package com.apea.compiler.ast;

public class IfStatement implements Statement {
    public final Expr condition;
    public final Statement thenClause;
    public final Statement elseClause;


    public IfStatement(Expr condition, Statement thenClause, Statement elseClause) {
        this.condition = condition;
        this.thenClause = thenClause;
        this.elseClause = elseClause;
    }

    public IfStatement(Expr condition, Statement thenClause) {
        this.condition = condition;
        this.thenClause = thenClause;
        this.elseClause = new EmptyStatement();
    }

    public boolean hasElseClause() {
        return !(elseClause instanceof EmptyStatement);
    }

    @Override
    public void accept(AstVisitor v) {v.visit(this);}

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof IfStatement)) return false;

        IfStatement that = (IfStatement) o;

        if (!condition.equals(that.condition)) return false;
        if (!thenClause.equals(that.thenClause)) return false;
        return elseClause.equals(that.elseClause);

    }

    @Override
    public String toString() {
        if (hasElseClause()) {
            return "if " + condition + " then " + thenClause + " else " + elseClause;
        } else {
            return "if " + condition + " then " + thenClause;
        }
    }
}
