package com.apea.compiler.ir;

import com.apea.compiler.types.OperatorType;

public class NamedOperatorType {
    public final OperatorType type;;
    public final String functionName;

    public NamedOperatorType(OperatorType type, String functionName) {
        this.type = type;
        this.functionName = functionName;
    }

    @Override
    public int hashCode() {
        int result = type != null ? type.hashCode() : 0;
        result = 31 * result + (functionName != null ? functionName.hashCode() : 0);
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof NamedOperatorType) {
            NamedOperatorType that = (NamedOperatorType)obj;
            return
                    this.type.equals(that.type) &&
                    this.functionName.equals(that.functionName);
        } else {
            return false;
        }
    }
}
