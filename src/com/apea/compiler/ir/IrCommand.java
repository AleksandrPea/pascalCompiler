package com.apea.compiler.ir;

public interface IrCommand {
    String getAssignedVar();
    
    void accept(IrVisitor visitor);
}
