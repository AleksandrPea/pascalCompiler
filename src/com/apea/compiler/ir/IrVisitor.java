package com.apea.compiler.ir;

public interface IrVisitor {
    default void visit(IrOperator call) {}
    default void visit(IrCopy copy) {}
    default void visit(IrLabel lable) {}
    default void visit(IrGoto g) {}
    default void visit(IrGotoIf gIf) {}
    default void visit(IrGotoIfNot gIfNot) {}
}
