package com.apea.compiler.types;

import java.util.*;

public class StandardTypes {
    private static final OperatorTable types = new OperatorTable();
    
    static {
        for (String op : new String[] { "+", "-", "*", "div", "mod" }) {
            OperatorType type = new OperatorType(typeList(IntegerType.instance, IntegerType.instance), IntegerType.instance);
            types.add(op, type);

        }
        for (String op : new String[] {"+", "-", "*", "/"}) {
            OperatorType type = new OperatorType(typeList(FloatType.instance, FloatType.instance), FloatType.instance);
            types.add(op, type);
        }
        
        for (String op : new String[] { "<", ">", "<=", ">=", "=", "<>" }) {
            OperatorType type = new OperatorType(typeList(IntegerType.instance, IntegerType.instance), BooleanType.instance);
            types.add(op, type);
            type = new OperatorType(typeList(FloatType.instance, FloatType.instance), BooleanType.instance);
            types.add(op, type);
        }

        for (String op : new String[] {"+", "-"}) {
            OperatorType type = new OperatorType(typeList(IntegerType.instance), IntegerType.instance);
            types.add(op, type);
            type = new OperatorType(typeList(FloatType.instance), FloatType.instance);
            types.add(op, type);
        }

        types.add("not", new OperatorType(typeList(BooleanType.instance), BooleanType.instance));
        for (String op : new String[]{"or", "and"}) {
            OperatorType type = new OperatorType(typeList(BooleanType.instance, BooleanType.instance), BooleanType.instance);
            types.add(op, type);
        }
        types.add("sqrt", new OperatorType(typeList(FloatType.instance), FloatType.instance));
        types.add("printFloat", new OperatorType(typeList(FloatType.instance), NoneType.instance));
        types.add("printInteger", new OperatorType(typeList(IntegerType.instance), NoneType.instance));
        types.add("printBoolean", new OperatorType(typeList(BooleanType.instance), NoneType.instance));
        types.add("print", new OperatorType(typeList(StringType.instance), NoneType.instance));
        types.add("println", new OperatorType(typeList(StringType.instance), NoneType.instance));
    }
    
    private static List<Type> typeList(Type... types) {
        return Arrays.asList(types);
    }

    public static OperatorTable getTypes() {
        return types;
    }
}