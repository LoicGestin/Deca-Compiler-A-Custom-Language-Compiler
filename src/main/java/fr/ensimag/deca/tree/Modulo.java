package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.codegen.codeGen;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.Type;
import fr.ensimag.ima.pseudocode.ImmediateInteger;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.instructions.*;

/**
 * @author gl29
 * @date 01/01/2024
 */
public class Modulo extends AbstractOpArith {

    public Modulo(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv,
                           ClassDefinition currentClass) throws ContextualError {
        Type tL = getLeftOperand().verifyExpr(compiler, localEnv, currentClass);
        Type tR = getRightOperand().verifyExpr(compiler, localEnv, currentClass);
        setType(tL);
        if (!tL.isInt() || !tR.isInt()) {
            throw new ContextualError("Invalid type for modulo : " + tL + "and " + tR, getLocation());
        }

        return getType();
    }

    @Override
    public void codeGenInst(DecacCompiler compiler) {
        AbstractExpr LValue = this.getLeftOperand();
        AbstractExpr RValue = this.getRightOperand();

        LValue.codeGenInst(compiler);
        RValue.codeGenInst(compiler);

        compiler.addInstruction(new REM(codeGen.getRegistreUtilise(),  codeGen.getCurrentRegistreUtilise()));
        if(!DecacCompiler.getNocheck()) {
            compiler.addInstruction(new BOV(compiler.getOverflow_error()));
        }

    }


    @Override
    protected String getOperatorName() {
        return "%";
    }

}
