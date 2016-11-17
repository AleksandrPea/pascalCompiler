package com.apea.compiler.ast;

public class BinaryOp implements Expr {
    public final Expr left;
    public final String opName;
    public final Expr right;

    public BinaryOp(Expr left, String opName, Expr right) {
        this.left = left;
        this.opName = opName;
        this.right = right;
    }
    public void accept(AstVisitor v) {
        v.visit(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BinaryOp)) return false;

        BinaryOp binaryOp = (BinaryOp) o;

        if (!left.equals(binaryOp.left)) return false;
        if (!opName.equals(binaryOp.opName)) return false;
        return right.equals(binaryOp.right);

    }

    @Override
    public String toString() {
        return "(" + left + " " + opName + " " + right + ")";
    }
}
