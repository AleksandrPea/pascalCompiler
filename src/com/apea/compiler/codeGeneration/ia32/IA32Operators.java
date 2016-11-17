package com.apea.compiler.codeGeneration.ia32;

import com.apea.compiler.ir.*;
import com.apea.compiler.tools.misc.NameMaker;
import com.apea.compiler.types.*;

import java.util.*;

class IA32Operators {
    private static final HashMap<NamedOperatorType, IA32Operator> operators = new HashMap<>();
    private static final NameMaker nameMarker = new NameMaker();

    static {
        OperatorTable opTable = StandardTypes.getTypes();
        Set<OperatorType> set = opTable.getSet("+");
        for (OperatorType type : set) {
            if (type.argTypes.size() == 1) {
                if (type.returnType.equals(IntegerType.instance)) {
                    operators.put(new NamedOperatorType(type, "+"), emptyOperator());
                } else if (type.returnType.equals(FloatType.instance)) {
                    operators.put(new NamedOperatorType(type, "+"), emptyOperator());
                }
            } else {
                if (type.returnType.equals(IntegerType.instance)) {
                    operators.put(new NamedOperatorType(type, "+"), binaryIntBoolOperator("add", true));
                } else if (type.returnType.equals(FloatType.instance)) {
                    operators.put(new NamedOperatorType(type, "+"), binaryFloatOperator("faddp"));
                }
            }
        }
        set = opTable.getSet("-");
        for (OperatorType type : set) {
            if (type.argTypes.size() == 1) {
                if (type.returnType.equals(IntegerType.instance)) {
                    operators.put(new NamedOperatorType(type, "-"), unaryIntegerOperator("neg"));
                } else if (type.returnType.equals(FloatType.instance)) {
                    operators.put(new NamedOperatorType(type, "-"), floatNegation());
                }
            } else {
                if (type.returnType.equals(IntegerType.instance)) {
                    operators.put(new NamedOperatorType(type, "-"), binaryIntBoolOperator("sub", true));
                }else if (type.returnType.equals(FloatType.instance)) {
                    operators.put(new NamedOperatorType(type, "-"), binaryFloatOperator("fsubp"));
                }
            }
        }
        set = opTable.getSet("*");
        for (OperatorType type : set) {
            if (type.returnType.equals(IntegerType.instance)) {
                operators.put(new NamedOperatorType(type, "*"), binaryIntBoolOperator("imul", true));
            } else if (type.returnType.equals(FloatType.instance)) {
                operators.put(new NamedOperatorType(type, "*"), binaryFloatOperator("fmulp"));
            }
        }
        set = opTable.getSet("div");
        operators.put(new NamedOperatorType(set.iterator().next(), "div"), divOperator());
        set = opTable.getSet("/");
        operators.put(new NamedOperatorType(set.iterator().next(), "/"), binaryFloatOperator("fdivp"));
        set = opTable.getSet("mod");
        operators.put(new NamedOperatorType(set.iterator().next(), "mod"), modOperator());
        set = opTable.getSet("<");
        for (OperatorType type : set) {
            if (type.argTypes.get(0).equals(IntegerType.instance)) {
                operators.put(new NamedOperatorType(type, "<"), integerCMP("jl"));
            } else if (type.argTypes.get(0).equals(FloatType.instance)) {
                operators.put(new NamedOperatorType(type, "<"), floatCMP("jb"));
            }
        }
        set = opTable.getSet(">");
        for (OperatorType type : set) {
            if (type.argTypes.get(0).equals(IntegerType.instance)) {
                operators.put(new NamedOperatorType(type, ">"), integerCMP("jg"));
            } else if (type.argTypes.get(0).equals(FloatType.instance)) {
                operators.put(new NamedOperatorType(type, ">"), floatCMP("ja"));
            }
        }
        set = opTable.getSet("<=");
        for (OperatorType type : set) {
            if (type.argTypes.get(0).equals(IntegerType.instance)) {
                operators.put(new NamedOperatorType(type, "<="), integerCMP("jle"));
            } else if (type.argTypes.get(0).equals(FloatType.instance)) {
                operators.put(new NamedOperatorType(type, "<="), floatCMP("jb"));
            }
        }
        set = opTable.getSet(">=");
        for (OperatorType type : set) {
            if (type.argTypes.get(0).equals(IntegerType.instance)) {
                operators.put(new NamedOperatorType(type, ">="), integerCMP("jge"));
            } else if (type.argTypes.get(0).equals(FloatType.instance)) {
                operators.put(new NamedOperatorType(type, ">="), floatCMP("ja"));
            }
        }
        set = opTable.getSet("=");
        for (OperatorType type : set) {
            if (type.argTypes.get(0).equals(IntegerType.instance)) {
                operators.put(new NamedOperatorType(type, "="), integerCMP("je"));
            } else if (type.argTypes.get(0).equals(FloatType.instance)) {
                operators.put(new NamedOperatorType(type, "="), floatCMP("je"));
            }
        }
        set = opTable.getSet("<>");
        for (OperatorType type : set) {
            if (type.argTypes.get(0).equals(IntegerType.instance)) {
                operators.put(new NamedOperatorType(type, "<>"), integerCMP("jn"));
            } else if (type.argTypes.get(0).equals(FloatType.instance)) {
                operators.put(new NamedOperatorType(type, "<>"), floatCMP("jn"));
            }
        }
        set = opTable.getSet("not");
        operators.put(new NamedOperatorType(set.iterator().next(), "not"), notOperator());
        set = opTable.getSet("and");
        operators.put(new NamedOperatorType(set.iterator().next(), "and"), binaryIntBoolOperator("and", false));
        set = opTable.getSet("or");
        operators.put(new NamedOperatorType(set.iterator().next(), "or"), binaryIntBoolOperator("or", false));
        set = opTable.getSet("sqrt");
        operators.put(new NamedOperatorType(set.iterator().next(), "sqrt"), sqrtOperator());
        set = opTable.getSet("printFloat");
        operators.put(new NamedOperatorType(set.iterator().next(), "printFloat"), printOperator("printFloat"));
        set = opTable.getSet("printInteger");
        operators.put(new NamedOperatorType(set.iterator().next(), "printInteger"), printOperator("printInteger"));
        set = opTable.getSet("printBoolean");
        operators.put(new NamedOperatorType(set.iterator().next(), "printBoolean"), printOperator("printBoolean"));
        set = opTable.getSet("print");
        operators.put(new NamedOperatorType(set.iterator().next(), "print"), printOperator("print"));
        set = opTable.getSet("println");
        operators.put(new NamedOperatorType(set.iterator().next(), "println"), printOperator("println"));
    }

    static String[] loadFloatConst(double val) {
        String[] result;
        if (val != 0) {
            result = new String[4];
            String strVal = Long.toHexString(Double.doubleToRawLongBits(val));
            String hBits = strVal.substring(0, 8) + "h";
            String lBits = strVal.substring(8, 16) + "h";
            if (!Character.isDigit(lBits.charAt(0))) {
                lBits = "0" + lBits;
            }
            if (!Character.isDigit(hBits.charAt(0))) {
                hBits = "0" + hBits;
            }
            result[0] = ";Loading float " + val;
            result[1] = "mov dword ptr[buf], " + lBits;
            result[2] = "mov dword ptr[buf+4], " + hBits;
            result[3] = "fld buf";
        } else {
            result = new String[1];
            result[0] = "fldz";
        }
        return result;
    }

    /** Пересилає рядок за початковою адресою [edi]. */
    static String[] movStringConst(String val) {
        byte[] bytes = val.getBytes();
        String[] result = new String[2*bytes.length + 2];
        result[0] = "; moving " + val+" to [edi]";
        for (int i = 0; i < bytes.length; i++) {
            result[2*i+1] = "mov dword ptr[edi], "+bytes[i];
            result[2*i+2] = "inc edi";
        }
        result[result.length - 1] = "; end moving string";
        return result;
    }


    private static void floatRValueToAsm(IrRValue rValue, ArrayList<String> code) {
        if (rValue instanceof IrFloatConst) {
            String[] lines = loadFloatConst(((IrFloatConst) rValue).value);
            for (String line : lines) {
                code.add(line);
            }
        } else {
            String value = IA32CodeGenerator.rvalueToAsm(rValue);
            if (!IA32CodeGenerator.isTempVar(value)) {
                code.add("fld " + value);
            }
        }
    }

    private static IA32Operator unaryIntegerOperator(final String asmOp) {
        return new IA32Operator(1) {
            @Override
            public String[] generate(IrRValue[] args) {
                String[] result = new String[2];
                String value = IA32CodeGenerator.rvalueToAsm(args[0]);
                if (IA32CodeGenerator.isTempVar(value)) {
                    result[0] = "pop eax";
                } else {
                    result[0] = "mov eax, " + value;
                }
                result[1] = asmOp+" eax";
                return result;
            }
        };
    }

    private static IA32Operator binaryIntBoolOperator(final String asmOp, boolean isIntegerOp) {
        return new IA32Operator(2) {
            @Override
            public String[] generate(IrRValue[] args) {
                ArrayList<String> result = new ArrayList<>();
                String reg;
                if (isIntegerOp) {
                    reg = "eax";
                } else {
                    reg = "al";
                }
                String arg2 = IA32CodeGenerator.rvalueToAsm(args[1]);
                if (IA32CodeGenerator.isTempVar(arg2)) {
                    result.add("pop ebx");
                    if (reg.equals("eax")) {
                        arg2 = "ebx";
                    } else if (reg.equals("al")) {
                        arg2 = "bl";
                    }
                }
                String arg1 = IA32CodeGenerator.rvalueToAsm(args[0]);
                if (IA32CodeGenerator.isTempVar(arg1)) {
                    result.add("pop eax");
                } else {
                    result.add("mov "+reg+", " + arg1);
                }
                result.add(asmOp + " "+reg+", " + arg2);
                return result.toArray(new String[result.size()]);
            }
        };
    }

    private static IA32Operator binaryFloatOperator(final String asmOp) {
        return new IA32Operator(2) {
            @Override
            public String[] generate(IrRValue[] args) {
                ArrayList<String> result = new ArrayList<>();
                floatRValueToAsm(args[0], result);
                floatRValueToAsm(args[1], result);
                result.add(asmOp + " ST(1), ST(0)");
                return result.toArray(new String[result.size()]);
            }
        };
    }

    private static IA32Operator floatNegation() {
        return new IA32Operator(1) {
            @Override
            public String[] generate(IrRValue[] args) {
                return binaryFloatOperator("fmulp").generate(new IrRValue[]{args[0], new IrFloatConst(-1.f)});
            }
        };
    }

    private static IA32Operator integerCMP(final String jxx) {
        return new IA32Operator(2) {
            @Override
            public String[] generate(IrRValue[] args) {
                String[] result;
                int i = 0;
                String arg2 = IA32CodeGenerator.rvalueToAsm(args[1]);
                if (IA32CodeGenerator.isTempVar(arg2)) {
                    result = new String[9];
                    result[i++] = "pop ebx";
                    arg2 = "ebx";
                } else {
                    result = new String[8];
                }
                String arg1 = IA32CodeGenerator.rvalueToAsm(args[0]);
                if (IA32CodeGenerator.isTempVar(arg1)) {
                    result[i++] = "pop eax";
                } else {
                    result[i++] = "mov eax, " + arg1;
                }
                String cmp = nameMarker.makeName("startcmp");
                String endcmp = nameMarker.makeName("endcmp");
                result[i++] = "cmp eax, " + arg2;
                result[i++] = jxx + " "+cmp;
                result[i++] = "xor al, al";
                result[i++] = "jmp " + endcmp;
                result[i++] = cmp+":";
                result[i++] = "mov al, 1";
                result[i] = endcmp+":";
                return result;
            }
        };
    }

    private static IA32Operator floatCMP(final String jxx) {
        return new IA32Operator(2) {
            @Override
            public String[] generate(IrRValue[] args) {
                ArrayList<String> result = new ArrayList<>();
                floatRValueToAsm(args[1], result);
                floatRValueToAsm(args[0], result);
                // в ST(0) - args[0], в ST(1) - args[1]
                String com = nameMarker.makeName("com");
                String endcom = nameMarker.makeName("endcom");
                result.add("fcomip ST(0), ST(1)");
                result.add("fstp ST(0)");
                result.add(jxx + " "+com);
                result.add("xor al, al");
                result.add("jmp " + endcom);
                result.add(com+":");
                result.add("mov al, 1");
                result.add(endcom+":");
                return result.toArray(new String[result.size()]);
            }
        };
    }

    private static IA32Operator notOperator() {
        return new IA32Operator(1) {
            @Override
            public String[] generate(IrRValue[] args) {
                String[] result = new String[2];
                String value = IA32CodeGenerator.rvalueToAsm(args[0]);
                if (IA32CodeGenerator.isTempVar(value)) {
                    result[0] = "pop eax";
                } else {
                    result[0] = "mov al, " + value;
                }
                result[1] = "not al";
                return result;
            }
        };
    }
    
    private static IA32Operator divOperator() {
        return new IA32Operator(1) {
            @Override
            public String[] generate(IrRValue[] args) {
                String[] result = new String[4];
                String arg = IA32CodeGenerator.rvalueToAsm(args[1]);
                if (IA32CodeGenerator.isTempVar(arg)) {
                    result[0] = "pop ebx";
                } else {
                    result[0] = "mov ebx, "+arg;
                }
                arg = IA32CodeGenerator.rvalueToAsm(args[0]);
                if (IA32CodeGenerator.isTempVar(arg)) {
                    result[1] = "pop eax";
                } else {
                    result[1] = "mov eax, " + arg;
                }
                result[2] = "cdq";        // розмножуємо знаковий розряд у edx
                result[3] = "idiv ebx";
                return result;
            }
        };
    }
    
    private static IA32Operator modOperator() {
        return new IA32Operator(2) {
            @Override
            public String[] generate(IrRValue[] args) {
                String[] lines = divOperator().generate(args);
                String[] result = Arrays.copyOf(lines, lines.length+1);
                result[lines.length] = "mov eax, edx";
                return result;
            }
        };
    }

    private static IA32Operator sqrtOperator() {
        return new IA32Operator(1) {
            @Override
            public String[] generate(IrRValue[] args) {
                ArrayList<String> result = new ArrayList<>();
                floatRValueToAsm(args[0], result);
                result.add("fsqrt");
                return result.toArray(new String[result.size()]);
            }
        };
    }

    private static IA32Operator printOperator(final String op) {
        return new IA32Operator(1) {
            @Override
            public String[] generate(IrRValue[] args) {
                ArrayList<String> result = new ArrayList<>();
                switch (op) {
                    case "printFloat":
                        floatRValueToAsm(args[0], result);
                        result.add("fstp buf");
                        result.add("printf(\"%f\", buf)");
                        break;
                    case "printInteger":case "printBoolean":
                        result.add("printf(\"%d\",  "+
                                IA32CodeGenerator.rvalueToAsm(args[0])+")");
                        break;
                    case "println":
                    case "print":
                        String value = IA32CodeGenerator.rvalueToAsm(args[0]);
                        if (args[0] instanceof IrStringConst) {
                            result.add("printf(\""+value+"\")");
                        } else if (args[0] instanceof IrVar) {
                            result.add("printf(offset "+value+")");
                        }
                        if (op.equals("println")) {
                            result.add("printf(\"\\n\")");
                        }
                }
                return result.toArray(new String[result.size()]);
            }
        };
    }

    private static IA32Operator emptyOperator() {
        return new IA32Operator(0) {
            @Override
            public String[] generate(IrRValue[] args) {
                return new String[0];
            }
        };
    }

    public static IA32Operator getOperator(NamedOperatorType opType) {
        return operators.get(opType);
    }

}