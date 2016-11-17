package com.apea.compiler.types;

import com.apea.compiler.tools.misc.StringUtils;

import java.util.List;

public class OperatorType implements Type {
    public final List<Type> argTypes;
    public final Type returnType;

    public OperatorType(List<Type> argTypes, Type returnType) {
        this.argTypes = argTypes;
        this.returnType = returnType;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof OperatorType) {
            OperatorType that = ((OperatorType)obj);
            return
                    this.argTypes.equals(that.argTypes) &&
                    this.returnType.equals(that.returnType);
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        int result = argTypes != null ? argTypes.hashCode() : 0;
        result = 31 * result + (returnType != null ? returnType.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "((" + StringUtils.join(argTypes, ", ") + ") -> " + returnType + ")";
    }
}


