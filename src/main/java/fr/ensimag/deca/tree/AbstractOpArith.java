package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.codegen.codeGen;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.Type;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.instructions.BOV;

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

        if (compiler.environmentType.cast_compatible(typeL, typeR)) {
            if (typeL.isFloat() && typeR.isInt()) {
                setRightOperand(new ConvFloat(getRightOperand()));
                typeR = getRightOperand().verifyExpr(compiler, localEnv, currentClass);
            } else if (typeL.isInt() && typeR.isFloat()) {
                setLeftOperand(new ConvFloat(getLeftOperand()));
                typeL = getLeftOperand().verifyExpr(compiler, localEnv, currentClass);
            }
        }

        if (!typeL.sameType(typeR)) {
            // Print left type and right type in debug mode
            throw new ContextualError("Exception : Incompatible types in arithmetic operation: " + typeL + " and " + typeR, getLocation());
        }

        setType(typeL);
        return getType();
    }

    public abstract void codeGenOperator(DecacCompiler compiler);

    @Override
    public void codeGenInst(DecacCompiler compiler) {
        codeGen.setAssignation(true);
        this.getLeftOperand().codeGenInst(compiler);
        codeGen.setAssignation(false);
        this.getRightOperand().codeGenInst(compiler);

        codeGenOperator(compiler);

        if (!DecacCompiler.getNocheck()) {
            compiler.addInstruction(new BOV(new Label("overflow_error")));
        }
    }

}
