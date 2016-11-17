package com.apea.compiler.codeGeneration.ia32;

import com.apea.compiler.ir.IrRValue;

abstract class IA32Operator {
    public final int argCount;

    public IA32Operator(int argCount) {
        this.argCount = argCount;
    }
    
    public abstract String[] generate(IrRValue[] args);
}
