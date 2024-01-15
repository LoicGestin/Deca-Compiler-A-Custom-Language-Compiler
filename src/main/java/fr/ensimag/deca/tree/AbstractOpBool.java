package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.codegen.codeGen;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.Type;

/**
 * @author gl29
 * @date 01/01/2024
 */
public abstract class AbstractOpBool extends AbstractBinaryExpr {

    public AbstractOpBool(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv,
                           ClassDefinition currentClass) throws ContextualError {
        Type typeL = getLeftOperand().verifyExpr(compiler, localEnv, currentClass);
        Type typeR = getRightOperand().verifyExpr(compiler, localEnv, currentClass);
        if (!typeL.isBoolean() || !typeR.isBoolean()) {
            throw new ContextualError("Exception : Incompatible types in boolean operation: " + typeL + " and " + typeR, getLocation());
        }
        setType(typeL);
        return getType();
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
