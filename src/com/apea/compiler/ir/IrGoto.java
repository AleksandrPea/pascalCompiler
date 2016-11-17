package com.apea.compiler.ir;

public class IrGoto implements IrCommand {
    public final String labelName;

    public IrGoto(String labelName) {
        this.labelName = labelName;
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
        if (obj instanceof IrGoto) {
            IrGoto that = (IrGoto)obj;
            return this.labelName.equals(that.labelName);
        } else {
            return false;
        }
    }

    @Override
    public String toString() {
        return "goto @" + labelName;
    }
}
