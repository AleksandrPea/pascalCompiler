package com.apea.compiler.ir;

public class IrLabel implements IrCommand {
    public final String name;

    public IrLabel(String name) {
        this.name = name;
    }
    
    @Override
    public void accept(IrVisitor visitor) {
        visitor.visit(this);
    }
    
    @Override
    public String getAssignedVar() {
        return null;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof IrLabel) {
            IrLabel that = (IrLabel)obj;
            return this.name.equals(that.name);
        } else {
            return false;
        }
    }

    @Override
    public String toString() {
        return "@" + name;
    }
}
