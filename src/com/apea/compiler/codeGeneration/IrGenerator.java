package com.apea.compiler.codeGeneration;

import com.apea.compiler.ast.*;
import com.apea.compiler.ir.*;
import com.apea.compiler.tools.misc.NameMaker;
import com.apea.compiler.types.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/** Генератор проміжного коду. */
public class IrGenerator {
    public static List<IrCommand> generate(Program program, Map<String, Type> varTable, OperatorTable defaultOpertaions) {
        IrGeneratorAstVisitor visitor = new IrGeneratorAstVisitor(varTable, defaultOpertaions);
        program.body.accept(visitor);
        return visitor.getOutput();
    }
    
    private static class IrGeneratorAstVisitor implements AstVisitor {
        private List<IrCommand> output = new ArrayList<>();
        private NameMaker nameMaker = new NameMaker();
        private IrRValue lastRValue;
        private Type lastType;
        private Map<String, Type> varTable;
        private OperatorTable operatorTable;

        public IrGeneratorAstVisitor(Map<String, Type> varTable, OperatorTable operatorTable) {
            this.varTable = varTable;
            this.operatorTable = operatorTable;
        }

        public List<IrCommand> getOutput() {
            return output;
        }

        @Override
        public void visit(StatementBlock statementBlock) {
            for (Statement statement : statementBlock.statements) {
                statement.accept(this);
            }
        }

        @Override
        public void visit(Assignment assignment) {
            assignment.expr.accept(this);
            add(new IrCopy(assignment.var.name, lastRValue));
        }

        @Override
        public void visit(FunctionCall call) {
            ArrayList<IrRValue> argRValues = new ArrayList<>(call.arguments.size());
            ArrayList<Type> argTypes = new ArrayList<>(call.arguments.size());
            for (Expr arg : call.arguments) {
                arg.accept(this);
                argRValues.add(lastRValue);
                argTypes.add(lastType);
            }
            OperatorType opType = operatorTable.search(call.functionName, argTypes);
            String assignedVar = null;
            if (!opType.returnType.equals(NoneType.instance)) {
                assignedVar = nameMaker.makeName("$resultOf_" + call.functionName + "_");
                varTable.put(assignedVar, opType.returnType);
            }
            add(new IrOperator(opType, assignedVar, call.functionName, argRValues));
        }

        @Override
        public void visit(IfStatement ifStmt) {
            String elseLabel = nameMaker.makeName("else");
            String endLabel = nameMaker.makeName("ifEnd");

            ifStmt.condition.accept(this);
            if (ifStmt.hasElseClause()) {
                add(new IrGotoIfNot(elseLabel, lastRValue));
            } else {
                add(new IrGotoIfNot(endLabel, lastRValue));
            }

            ifStmt.thenClause.accept(this);
            if (ifStmt.hasElseClause()) {
                add(new IrGoto(endLabel));

                add(new IrLabel(elseLabel));
                ifStmt.elseClause.accept(this);
            }

            add(new IrLabel(endLabel));
        }

        @Override
        public void visit(WhileStatement whileStatement) {
            String bodyLabel = nameMaker.makeName("whileBody");
            String endLabel = nameMaker.makeName("whileEnd");

            add(new IrLabel(bodyLabel));

            whileStatement.condition.accept(this);
            add(new IrGotoIfNot(endLabel, lastRValue));

            whileStatement.body.accept(this);
            add(new IrGoto(bodyLabel));

            add(new IrLabel(endLabel));
        }

        @Override
        public void visit(ForStatement forStatement) {
            add(new IrCopy(forStatement.var.name,
                    new IrIntegerConst(forStatement.left.value)));

            String bodyLabel = nameMaker.makeName("forBody");
            String endLabel = nameMaker.makeName("forEnd");

            add(new IrLabel(bodyLabel));
            OperatorType opType = operatorTable.search("<=",
                    IntegerType.instance, IntegerType.instance);
            String assignedVar = nameMaker.makeName("$resultOf_<=_");
            varTable.put(assignedVar, opType.returnType);
            add(new IrOperator(opType, assignedVar, "<=", new IrVar(forStatement.var.name),
                    new IrIntegerConst(forStatement.right.value)));
            add(new IrGotoIfNot(endLabel, new IrVar(assignedVar)));

            forStatement.body.accept(this);
            add(new IrGoto(bodyLabel));

            add(new IrLabel(endLabel));
        }

        @Override
        public void visit(RepeatStatement repeatStatement) {
            String bodyLabel = nameMaker.makeName("repeatBody");

            add(new IrLabel(bodyLabel));
            repeatStatement.body.accept(this);

            repeatStatement.condition.accept(this);
            add(new IrGotoIfNot(bodyLabel, lastRValue));
        }

        @Override
        public void visit(BinaryOp binop) {
            ArrayList<Type> args = new ArrayList<>();
            binop.left.accept(this);
            IrRValue leftVal = lastRValue;
            args.add(lastType);
            binop.right.accept(this);
            IrRValue rightVal = lastRValue;
            args.add(lastType);
            OperatorType opType = operatorTable.search(binop.opName, args);
            String assignedVar = nameMaker.makeName("$resultOf_" + binop.opName + "_");
            varTable.put(assignedVar, opType.returnType);
            add(new IrOperator(opType, assignedVar, binop.opName, leftVal, rightVal));
        }

        @Override
        public void visit(UnaryOp unop) {
            ArrayList<Type> args = new ArrayList<>();
            unop.operand.accept(this);
            IrRValue operandVal = lastRValue;
            args.add(lastType);
            OperatorType opType = operatorTable.search(unop.opName, args);
            String assignedVar = nameMaker.makeName("$resultOf_" + unop.opName + "_");
            varTable.put(assignedVar, opType.returnType);
            add(new IrOperator(opType,assignedVar, unop.opName, operandVal));
        }

        @Override
        public void visit(IntegerConst intConst) {
            lastRValue = new IrIntegerConst(intConst.value);
            lastType = IntegerType.instance;
        }

        @Override
        public void visit(BooleanConst boolConst) {
            lastRValue = new IrBooleanConst(boolConst.value);
            lastType = BooleanType.instance;
        }

        @Override
        public void visit(FloatConst floatConst) {
            lastRValue = new IrFloatConst(floatConst.value);
            lastType = FloatType.instance;
        }


        @Override
        public void visit(StringConst strConst) {
            lastRValue = new IrStringConst(strConst.value);
            lastType = StringType.instance;
        }

        @Override
        public void visit(Var var) {
            lastRValue = new IrVar(var.name);
            lastType = varTable.get(var.name);
        }

        private void add(IrCommand command) {
            output.add(command);
            if (command.getAssignedVar() != null) {
                lastRValue = new IrVar(command.getAssignedVar());
                Type type = varTable.get(command.getAssignedVar());
                if (type != null) {
                    lastType = type;
                } else {
                    System.out.println("err");
                }
            }
        }
    };
}
