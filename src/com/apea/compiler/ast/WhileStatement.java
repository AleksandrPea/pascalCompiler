package com.apea.compiler.ast;

public class WhileStatement implements Statement {

    public final Expr condition;
    public final Statement body;

    public WhileStatement(Expr condition, Statement body) {
        this.condition = condition;
        this.body = body;
    }

    public void accept(AstVisitor v) {
        v.visit(this);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof WhileStatement) {
            WhileStatement that = (WhileStatement)obj;
            return
                    this.condition.equals(that.condition) &&
                            this.body.equals(that.body);
        } else {
            return false;
        }
    }

    @Override
    public String toString() {
        return "while " + condition + " do " + body;
    }
}
