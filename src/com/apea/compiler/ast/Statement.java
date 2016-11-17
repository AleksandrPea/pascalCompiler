package com.apea.compiler.ast;

public interface Statement {

    public void accept(AstVisitor v);
}
