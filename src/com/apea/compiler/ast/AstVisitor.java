package com.apea.compiler.ast;

public interface AstVisitor {

    default void visit(FunctionCall functionCall) {}
    default void visit(Definition def) {}
    default void visit(DefinitionBlock defBlock) {}
    default void visit(StatementBlock statementBlock) {}
    default void visit(Assignment assignment) {}
    default void visit(EmptyStatement empty) {}
    default void visit(IfStatement ifStmt) {}
    default void visit(WhileStatement whileStmt) {}
    default void visit(ForStatement forStatement) {}
    default void visit(RepeatStatement repeatStatement) {}
    default void visit(BinaryOp binop) {}
    default void visit(UnaryOp unop) {}
    default void visit(IntegerConst integerConst) {}
    default void visit(FloatConst intConst) {}
    default void visit(StringConst strConst) {}
    default void visit(BooleanConst booleanConst) {}
    default void visit(Var var) {}
    default void visit(ArrayVar arrayVar) {}
}
