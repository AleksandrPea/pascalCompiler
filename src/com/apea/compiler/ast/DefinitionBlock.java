package com.apea.compiler.ast;

import com.apea.compiler.tools.misc.StringUtils;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class DefinitionBlock implements Statement  {
    public final List<Definition> definitions;

    public DefinitionBlock(List<Definition> definitions) {
        this.definitions = Collections.unmodifiableList(definitions);
    }

    public DefinitionBlock(Definition... definitions) {
        this(Arrays.asList(definitions));
    }

    public void accept(AstVisitor v) {
        v.visit(this);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof DefinitionBlock) {
            DefinitionBlock that = (DefinitionBlock)obj;
            return this.definitions.equals(that.definitions);
        } else {
            return false;
        }
    }

    @Override
    public String toString() {
        return "var\n"+StringUtils.join(definitions, ";\n")+";\n";
    }


}
