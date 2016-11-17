package com.apea.compiler.semantic;

import com.apea.compiler.ast.*;
import com.apea.compiler.types.*;

import java.util.*;

/** Представляє семантичний аналізатор. */
public class TypeChecker {
    public static HashMap<String, Type> checkTypes(Program program, OperatorTable defaultOpertaions) {
        TypeCheckVisitor visitor = new TypeCheckVisitor(defaultOpertaions);
        program.defPart.accept(visitor);
        program.body.accept(visitor);
        return visitor.varTable;
    }

    private static class TypeCheckVisitor implements AstVisitor {
        private HashMap<String, Type> varTable;
        private OperatorTable operatorTable;
        private Type lastType;

        public TypeCheckVisitor(OperatorTable operatorTypes) {
            varTable = new HashMap<>();
            operatorTable = new OperatorTable();
            operatorTable.addAll(operatorTypes);
            lastType = NoneType.instance;
        }

        @Override
        public void visit(Definition def) {
            if (varTable.containsKey(def.varName)){
                throw new TypeException("Variable " + def.varName+" already defined.");
            }
            varTable.put(def.varName, def.type);
        }

        @Override
        public void visit(Assignment assignment) {
            Type varType = varTable.get(assignment.var.name);
            if (varType == null) {
                throw new TypeException("Assignment to undefined variable: " + assignment.var);
            }
            assignment.expr.accept(this);
            if (!canBeAssigned(lastType, varType)) {
                throw new TypeException("Cannot assign " + lastType + " to '" + assignment.var + "' (of type "+varType+") ");
            }
            lastType = NoneType.instance;
        }

        @Override
        public void visit(StatementBlock statementBlock) {
            for (Statement statement : statementBlock.statements) {
                statement.accept(this);
            }
            lastType = NoneType.instance;
        }

        @Override
        public void visit(DefinitionBlock defBlock) {
            for (Definition definition : defBlock.definitions) {
                definition.accept(this);
            }
        }

        @Override
        public void visit(FunctionCall call) {
            ArrayList<Type> argTypes = new ArrayList<>(call.arguments.size());
            for (Expr argExpr : call.arguments) {
                argExpr.accept(this);
                argTypes.add(lastType);
            }

            checkOperatorType(call.functionName, argTypes);
        }

        @Override
        public void visit(IfStatement ifStmt) {
            checkCondition(ifStmt.condition);
            ifStmt.thenClause.accept(this);
            ifStmt.elseClause.accept(this);
            lastType = NoneType.instance;
        }

        @Override
        public void visit(WhileStatement whileStatement) {
            checkCondition(whileStatement.condition);
            whileStatement.body.accept(this);
            lastType = NoneType.instance;
        }

        @Override
        public void visit(ForStatement forStatement) {
            forStatement.var.accept(this);
            if (!canBeAssigned(lastType, IntegerType.instance)) {
                throw new TypeException("Cannot assign integer const to '" +
                        forStatement.var + "' (of type "+lastType+") ");
            }
            if (forStatement.right.value <= forStatement.left.value) {
                throw new TypeException("Error in for loop: " +
                forStatement.right.value + " <= " + forStatement.left);
            }
            forStatement.body.accept(this);
            lastType = NoneType.instance;
        }

        @Override
        public void visit(RepeatStatement repeatStatement) {
            checkCondition(repeatStatement.condition);
            repeatStatement.body.accept(this);
            lastType = NoneType.instance;
        }

        @Override
        public void visit(UnaryOp unop) {
            unop.operand.accept(this);
            checkOperatorType(unop.opName, lastType);
        }

        @Override
        public void visit(BinaryOp binop) {
            binop.left.accept(this);
            Type leftType = lastType;
            binop.right.accept(this);
            Type rightType = lastType;
            checkOperatorType(binop.opName, leftType, rightType);
        }

        @Override
        public void visit(IntegerConst integerConst) {
            lastType = IntegerType.instance;
        }

        @Override
        public void visit(FloatConst intConst) {
            lastType = FloatType.instance;
        }

        @Override
        public void visit(BooleanConst boolConst) {
            lastType = BooleanType.instance;
        }

        @Override
        public void visit(StringConst strConst) { lastType = StringType.instance;}

        @Override
        public void visit(Var var) {
            lastType = varTable.get(var.name);
            if (lastType == null) {
                throw new TypeException("Unknown variable: " + var.name);
            }
        }

        private void checkOperatorType(String opName, List<Type> givenArgTypes) {
            if (!operatorTable.containsKey(opName)) {
                throw new TypeException("Unknown operator "+opName);
            }
            StringBuilder msg = new StringBuilder("Possible variants: ");
            Iterator<OperatorType> iter = operatorTable.getSet(opName).iterator();
            boolean flag = true;
            while (iter.hasNext() && flag) {
                OperatorType opType = iter.next();
                if (opType.argTypes.size() != givenArgTypes.size()) {
                    msg.append("\n"+opName + " expects " + opType.argTypes + " but " + givenArgTypes.size() + " arguments given");
                } else {
                    boolean endCycle = false;
                    for (int i = 0; i < givenArgTypes.size() && !endCycle; i++) {
                        Type given = givenArgTypes.get(i);
                        Type expected = opType.argTypes.get(i);
                        if (!canBeAssigned(given, expected)) {
                            msg.append("\n"+opName + " argument " + (i + 1) + " expects " + expected + " but " + given + " given");
                            endCycle = true;
                        }
                    }
                    if (!endCycle) {
                        flag = false;
                        lastType = opType.returnType;
                    }
                }
            }
            if (flag) {
                throw new TypeException(msg.toString());
            }
        }

        private void checkCondition(Expr condition) {
            condition.accept(this);
            if (!lastType.equals(BooleanType.instance)) {
                throw new TypeException("Condition was " + lastType + " instead of boolean");
            } else if(condition instanceof BooleanConst) {
                BooleanConst booleanConst = (BooleanConst)condition;
                if (booleanConst.value) {
                    throw new TypeException("There is infinite loop or senseless code");
                } else {
                    throw new TypeException("Condition is always false(may be unreachable code)");
                }
            }
        }

        private void checkOperatorType(String funcName, Type... givenArgTypes) {
            checkOperatorType(funcName, Arrays.asList(givenArgTypes));
        }

        private boolean canBeAssigned(Type from, Type to) {
            return from.equals(to);
        }
    }
}
