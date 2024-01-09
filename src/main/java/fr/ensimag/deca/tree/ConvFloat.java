package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.*;
import fr.ensimag.deca.DecacCompiler;

/**
 * Conversion of an int into a float. Used for implicit conversions.
 * 
 * @author gl29
 * @date 01/01/2024
 */
public class ConvFloat extends AbstractUnaryExpr {
    public ConvFloat(AbstractExpr operand) {
        super(operand);
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass) throws ContextualError {
        Type typeOp = getOperand().verifyExpr(compiler, localEnv, currentClass);
        Type floatType = new FloatType(compiler.createSymbol("float"));
        if (typeOp.isInt()) {
            setType(floatType);
            return getType();
        }
        else {
            throw new ContextualError("Conversion impossible", getLocation());
        }

    }


    @Override
    protected String getOperatorName() {
        return "/* conv float */";
    }

}
