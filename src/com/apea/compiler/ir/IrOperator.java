package com.apea.compiler.ir;

import com.apea.compiler.tools.misc.StringUtils;
import com.apea.compiler.types.OperatorType;

import java.util.Arrays;
import java.util.List;

public class IrOperator implements IrCommand {
    public final NamedOperatorType namedType;
    public final String assignedVar;
    public final List<IrRValue> args;

    public IrOperator(OperatorType namedType, String returnVar, String functionName, List<IrRValue> args) {
        this.namedType = new NamedOperatorType(namedType, functionName);
        this.assignedVar = returnVar;
        this.args = args;
    }
    
    public IrOperator(OperatorType namedType, String returnVar, String functionName, IrRValue... args) {
        this(namedType, returnVar, functionName, Arrays.asList(args));
    }
    
    @Override
    public void accept(IrVisitor visitor) {
        visitor.visit(this);
    }
    
    @Override
    public String getAssignedVar() {
        return assignedVar;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof IrOperator) {
            IrOperator that = (IrOperator)obj;
            return
                    this.namedType.equals(that.namedType) &&
                    this.assignedVar.equals(that.assignedVar) &&
                    this.args.equals(that.args);
        } else {
            return false;
        }
    }

    @Override
    public String toString() {
        return assignedVar + " := " + namedType.functionName + "(" + StringUtils.join(args, ", ") + ")";
    }
}
