package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.Type;

/**
 * Arithmetic binary operations (+, -, /, ...)
 *
 * @author gl29
 * @date 01/01/2024
 */
public abstract class AbstractOpArith extends AbstractBinaryExpr {

    public AbstractOpArith(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv,
                           ClassDefinition currentClass) throws ContextualError {
        Type typeL = getLeftOperand().verifyExpr(compiler, localEnv, currentClass);
        Type typeR = getRightOperand().verifyExpr(compiler, localEnv, currentClass);
        if (compiler.environmentType.compatible(typeL, typeR)) {
            if (typeL.isFloat() && typeR.isInt()) {
                typeR = new ConvFloat(getRightOperand()).verifyExpr(compiler, localEnv, currentClass);
                setRightOperand(new ConvFloat(getRightOperand()));
                getRightOperand().verifyExpr(compiler, localEnv, currentClass);
            } else if (typeL.isInt() && typeR.isFloat()) {
                typeL = new ConvFloat(getLeftOperand()).verifyExpr(compiler, localEnv, currentClass);
                setLeftOperand(new ConvFloat(getLeftOperand()));
                getLeftOperand().verifyExpr(compiler, localEnv, currentClass);
            }
        }
        if (!typeL.sameType(typeR)) {
            // Print left type and right type in debug mode
            throw new ContextualError("Exception : Incompatible types in arithmetic operation: " + typeL + " and " + typeR, getLocation());
        }
        setType(typeL);
        return getType();
    }

}
