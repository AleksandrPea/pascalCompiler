package com.apea.compiler.ir;

public class IrBooleanConst implements IrRValue {
    public final boolean value;

    public IrBooleanConst(boolean value) {
        this.value = value;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof IrBooleanConst) {
            IrBooleanConst that = (IrBooleanConst)obj;
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
