package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;

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
        Type typeL = getLeftOperand().getType();
        Type typeR = getRightOperand().getType();
        if(compiler.environmentType.compatible(typeL, typeR)) {
            if (typeL.isFloat() && typeR.isInt()) {
                setRightOperand(new ConvFloat(getRightOperand()));
            } else if (typeL.isInt() && typeR.isFloat()) {
                setLeftOperand(new ConvFloat(getLeftOperand()));

            }
        }
        getLeftOperand().verifyExpr(compiler, localEnv, currentClass);
        getRightOperand().verifyExpr(compiler, localEnv, currentClass);

        setType(getLeftOperand().getType());
        return getType();
    }
}
