package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.codegen.codeGen;
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
        if (compiler.environmentType.cast_compatible(t1, t2) && (t1.isInt() || t1.isFloat() || t1.isBoolean())) {
            if (t1.isFloat() && t2.isInt()) {
                setRightOperand(new ConvFloat(getRightOperand()));
                getRightOperand().verifyExpr(compiler, localEnv, currentClass);
            } else if (t1.isInt() && t2.isFloat()) {
                setLeftOperand(new ConvFloat(getLeftOperand()));
                getLeftOperand().verifyExpr(compiler, localEnv, currentClass);
            }
        } else {
            throw new ContextualError("Exception : type incompatible : " + t1 + " and " + t2, this.getLocation());
        }

        this.setType(compiler.environmentType.BOOLEAN);
        return this.getType();
    }

    public abstract void codeGenOp(DecacCompiler compiler);

    @Override
    protected void codeGenInst(DecacCompiler compiler) {
        codeGen.setAssignation(true);
        getLeftOperand().codeGenInst(compiler);
        codeGen.setAssignation(false);
        getRightOperand().codeGenInst(compiler);
        codeGenOp(compiler);
    }


}
