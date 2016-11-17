package com.apea.compiler.ast;

import com.apea.compiler.types.Type;

public class Definition implements Statement {
    public final String varName;
    public final Type type;

    public Definition(String varName, Type type) {
        this.varName = varName;
        this.type = type;
    }

    public void accept(AstVisitor v) {
        v.visit(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Definition)) return false;

        Definition that = (Definition) o;

        if (!varName.equals(that.varName)) return false;
        return type.equals(that.type);

    }

    @Override
    public String toString() {
        return varName + " : " + type;
    }
}
