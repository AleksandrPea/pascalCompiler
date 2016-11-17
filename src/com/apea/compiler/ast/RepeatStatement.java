package com.apea.compiler.ast;

public class RepeatStatement implements Statement {

    public final Statement body;
    public final Expr condition;

    public RepeatStatement(Statement body, Expr condition) {
        this.condition = condition;
        this.body = body;
    }

    public void accept(AstVisitor v) {
        v.visit(this);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof RepeatStatement) {
            RepeatStatement that = (RepeatStatement)obj;
            return
                    this.condition.equals(that.condition) &&
                            this.body.equals(that.body);
        } else {
            return false;
        }
    }

    @Override
    public String toString() {
        return "repeat " + body + " until "+condition;
    }
}
