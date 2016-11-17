package com.apea.compiler.codeGeneration.ia32;

import com.apea.compiler.ir.*;
import com.apea.compiler.types.*;

import java.util.*;

class IA32ProgramVisitor implements IrVisitor {
    private List<IrCommand> input;
    private Map<String, Type> varTable;
    private List<String> output;

    public IA32ProgramVisitor(Map<String, Type> varTable, List<IrCommand> input) {
        this.input = input;
        this.varTable = varTable;
        this.output = new ArrayList<>();
    }
    
    public List<String> generate() {
        output.clear();
        generateProgramStart();
        generateDefinitions();
        generateBody();
        return output;
    }
    
    private void generateProgramStart() {
        addLine(".686");
        addLine(".model flat, stdcall");
        addLine("option casemap :none");
        addLine("include \\masm32\\include\\windows.inc");
        addLine("include \\masm32\\include\\masm32.inc");
        addLine("include \\masm32\\include\\user32.inc");
        addLine("include \\masm32\\include\\kernel32.inc");
        addLine("include \\masm32\\include\\msvcrt.inc");
        addLine("include \\masm32\\macros\\macros.asm");
        addLine("includelib \\masm32\\lib\\masm32.lib");
        addLine("includelib \\masm32\\lib\\user32.lib");
        addLine("includelib \\masm32\\lib\\kernel32.lib");
        addLine("includelib \\masm32\\lib\\msvcrt.lib");
    }

    private void generateDefinitions() {
        addLine(".data");
        addLine("buf dq 0");
        Iterator<Map.Entry<String, Type>> iter =  varTable.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry<String, Type> entry = iter.next();
            if (!IA32CodeGenerator.isTempVar("_" + entry.getKey())) {
                if (entry.getValue().equals(IntegerType.instance)) {
                    addLine("_" + entry.getKey() + " dd 0");
                } else if(entry.getValue().equals(FloatType.instance)) {
                    addLine("_" + entry.getKey() + " dq 0");
                } else if (entry.getValue().equals(BooleanType.instance)) {
                    addLine("_" + entry.getKey() + " db 0");
                } else if (entry.getValue().equals(StringType.instance)) {
                    addLine("_" + entry.getKey() + " db 256 dup(?)");
                }
            }
        }
    }

    private void generateBody() {
        addLine(".code");
        addLine("main:");
        for (IrCommand cmd : input) {
            cmd.accept(this);
        }
        addLine("INVOKE ExitProcess, 0");
        addLine("end main");
    }

    @Override
    public void visit(IrCopy copy) {
        Type type = varTable.get(copy.assignedVar);
        String var = irNameToAsm(copy.assignedVar);
        String value = IA32CodeGenerator.rvalueToAsm(copy.rvalue);
        if (type.equals(IntegerType.instance) || type.equals(BooleanType.instance)) {
            String reg;
            if (type.equals(IntegerType.instance)) {
                reg = "eax";
            } else {
                reg = "al";
            }
            if (copy.rvalue instanceof IrVar) {
                if (IA32CodeGenerator.isTempVar(value)) {
                    addLine("pop eax");
                } else {
                    addLine("mov "+reg+", " + value);
                }
                addLine("mov "+ var+", "+reg);
            } else {
                addLine("mov " + var+ ", "+value);
            }
        } else if (type.equals(FloatType.instance)) {
            if (copy.rvalue instanceof IrFloatConst) {
                String[] code = IA32Operators.loadFloatConst(((IrFloatConst) copy.rvalue).value);
                for (String str : code) {
                    addLine(str);
                }
            } else if (!IA32CodeGenerator.isTempVar(value)) {
                addLine("fld " + value);
            }
            addLine("fstp "+ var);
        } else if (type.equals(StringType.instance)) {
            addLine("mov edi, offset "+var);
            if (copy.rvalue instanceof IrStringConst) {
                String[] code = IA32Operators.movStringConst(value);
                for (String str : code) {
                    addLine(str);
                }
            } else {
                addLine("mov esi, offset " + value);
                addLine("mov ecx, 256");
                addLine("rep movsb");
            }
        }
    }

    @Override
    public void visit(IrOperator operator) {
        IA32Operator ia32operator = IA32Operators.getOperator(operator.namedType);
        String[] lines = ia32operator.generate(operator.args.toArray(new IrRValue[operator.args.size()]));
        for (String line : lines) {
            addLine(line);
        }
        Type rType = operator.namedType.type.returnType;
        if (operator.assignedVar != null) {
            if (rType.equals(IntegerType.instance) || rType.equals(BooleanType.instance)) {
                addLine("push eax");
            }
        }
    }

    @Override
    public void visit(IrGoto g) {
        addLine("jmp " + g.labelName);
    }

    @Override
    public void visit(IrGotoIf gIf) {
        String condition = IA32CodeGenerator.rvalueToAsm(gIf.condition);
        if (IA32CodeGenerator.isTempVar(condition)) {
            addLine("pop eax");
        } else {
            addLine("mov al, " + IA32CodeGenerator.rvalueToAsm(gIf.condition));
        }
        addLine("cmp al, 0");
        addLine("jne " + gIf.labelName);
    }

    @Override
    public void visit(IrGotoIfNot gIfNot) {
        String condition = IA32CodeGenerator.rvalueToAsm(gIfNot.condition);
        if (IA32CodeGenerator.isTempVar(condition)) {
            addLine("pop eax");
        } else {
            addLine("mov al, " + IA32CodeGenerator.rvalueToAsm(gIfNot.condition));
        }
        addLine("cmp al, 0");
        addLine("je " + gIfNot.labelName);
    }

    @Override
    public void visit(IrLabel g) {
        addLine(g.name + ":");
    }

    private String irNameToAsm(String irVar) {
        return "_"+irVar;
    }
    
    private void addLine(String line) {
        if (!line.endsWith(":") && !line.startsWith(".") && !line.startsWith("end")) {
            output.add("    " + line);
        } else {
            output.add(line);
        }
    }
}
