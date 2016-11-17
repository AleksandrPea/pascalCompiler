package com.apea.compiler.codeGeneration.ia32;

import com.apea.compiler.ir.*;
import com.apea.compiler.types.Type;

import java.util.List;
import java.util.Map;

/** Генератор проміжного коду. */
public class IA32CodeGenerator {
    public static List<String> generateAsmProgram(Map<String, Type> varTable, List<IrCommand> programBody) {
      return new IA32ProgramVisitor(varTable, programBody).generate();
    }

    public static String rvalueToAsm(IrRValue rv) {
        if (rv instanceof IrIntegerConst) {
            return "" + ((IrIntegerConst)rv).value;
        } else if (rv instanceof IrFloatConst) {
            return "" + ((IrFloatConst)rv).value;
        } else  if (rv instanceof IrBooleanConst) {
            boolean value = ((IrBooleanConst)rv).value;
            return value ? "1" : "0";
        } else if (rv instanceof IrStringConst) {
            return ((IrStringConst) rv).value;
        } else if (rv instanceof IrVar) {
            return "_"+((IrVar) rv).name;
        } else {
            throw new IllegalArgumentException();
        }
    }

    static boolean isTempVar(String var) {
        return var.startsWith("_$");
    }
}