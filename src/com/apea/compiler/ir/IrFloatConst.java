package com.apea.compiler.ir;

public class IrFloatConst implements IrRValue {
    public final double value;

    public IrFloatConst(double value) {
        this.value = value;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof IrFloatConst) {
            IrFloatConst that = (IrFloatConst)obj;
            return this.value == that.value;
        } else {
            return false;
        }
    }

    @Override
    public String toString() {
        return "" + value;
    }
}
