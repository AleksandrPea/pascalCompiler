package com.apea.compiler.ir;

public class IrIntegerConst implements IrRValue {
    public final int value;

    public IrIntegerConst(int value) {
        this.value = value;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof IrIntegerConst) {
            IrIntegerConst that = (IrIntegerConst)obj;
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
