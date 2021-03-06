package com.apea.compiler.ir;

public class IrGotoIfNot implements IrCommand {
    public final String labelName;
    public final IrRValue condition;

    public IrGotoIfNot(String labelName, IrRValue condition) {
        this.labelName = labelName;
        this.condition = condition;
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
        if (obj instanceof IrGotoIfNot) {
            IrGotoIfNot that = (IrGotoIfNot)obj;
            return
                    this.labelName.equals(that.labelName) &&
                    this.condition.equals(that.condition);
        } else {
            return false;
        }
    }

    @Override
    public String toString() {
        return "goto @" + labelName + " if not " + condition;
    }
}
