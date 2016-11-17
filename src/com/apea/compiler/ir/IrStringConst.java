package com.apea.compiler.ir;

public class IrStringConst implements IrRValue {
    public final String value;

    public IrStringConst(String value) {
        this.value = value;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof IrStringConst) {
            IrStringConst that = (IrStringConst)obj;
            return this.value == that.value;
        } else {
            return false;
        }
    }

    @Override
    public String toString() {
        return "\""+value+"\"";
    }
}
