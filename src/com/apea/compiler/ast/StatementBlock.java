package com.apea.compiler.ast;

import com.apea.compiler.tools.misc.StringUtils;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class StatementBlock implements Statement {
    public final List<Statement> statements;

    public StatementBlock(List<Statement> statements) {
        this.statements = Collections.unmodifiableList(statements);
    }

    public StatementBlock(Statement... statements) {
        this(Arrays.asList(statements));
    }

    public void accept(AstVisitor v) {
        v.visit(this);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof StatementBlock) {
            StatementBlock that = (StatementBlock)obj;
            return this.statements.equals(that.statements);
        } else {
            return false;
        }
    }

    @Override
    public String toString() {
        return "begin\n"+StringUtils.join(statements, ";\n")+"\nend";
    }
}
