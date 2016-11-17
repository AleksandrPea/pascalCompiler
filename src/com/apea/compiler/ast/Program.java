package com.apea.compiler.ast;

public class Program {

    public final String name;
    public final DefinitionBlock defPart;
    public final StatementBlock body;

    public Program(String name, DefinitionBlock defPart, StatementBlock body) {
        this.name = name;
        this.body = body;
        this.defPart = defPart;
    }

    @Override
    public String toString() {
        return "program "+name+";\n"+defPart+body+".";
    }
}
