package com.apea.compiler.ir;

public class IrVar implements IrRValue {
    public final String name;

    public IrVar(String name) {
        this.name = name;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof IrVar) {
            IrVar that = (IrVar)obj;
            return this.name.equals(that.name);
        } else {
            return false;
        }
    }

    @Override
    public String toString() {
        return name;
    }
}
