package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.*;

/**
 * @author gl29
 * @date 01/01/2024
 */
public abstract class AbstractOpCmp extends AbstractBinaryExpr {

    public AbstractOpCmp(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv,
                           ClassDefinition currentClass) throws ContextualError {
        Type t1 = this.getLeftOperand().verifyExpr(compiler, localEnv, currentClass);
        Type t2 = this.getRightOperand().verifyExpr(compiler, localEnv, currentClass);
        if (compiler.environmentType.compatible(t1, t2)) {
            if (t1.isFloat() && t2.isInt()) {
                t2 = new ConvFloat(getRightOperand()).verifyExpr(compiler, localEnv, currentClass);
                setRightOperand(new ConvFloat(getRightOperand()));
                getRightOperand().verifyExpr(compiler, localEnv, currentClass);
            } else if (t1.isInt() && t2.isFloat()) {
                t1 = new ConvFloat(getLeftOperand()).verifyExpr(compiler, localEnv, currentClass);
                setLeftOperand(new ConvFloat(getLeftOperand()));
                getLeftOperand().verifyExpr(compiler, localEnv, currentClass);
            }
        } else {
            throw new ContextualError("Exception : type incompatible : " + t1 + " and " + t2, this.getLocation());
        }

        if (t1.isInt() || t1.isFloat() || t1.isBoolean() || t1.isString()) {
            this.setType(new BooleanType(compiler.createSymbol("boolean")));
            return this.getType();
        } else {
            throw new ContextualError("Exception : type incompatible : " + t1 + " and " + t2, this.getLocation());
        }
    }


}
