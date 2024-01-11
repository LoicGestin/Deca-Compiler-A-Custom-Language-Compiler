package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
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

        Label divByZero = compiler.labelTable.addLabel("divByZero");
        Label fin = compiler.labelTable.addLabel("fin");

        LValue.codeGenInst(compiler);
        RValue.codeGenInst(compiler);
        int number = compiler.getNextRegistreLibre().getNumber();

        compiler.addInstruction(new CMP(new ImmediateInteger(0), compiler.getRegister(number-1)));
        compiler.addInstruction(new BEQ(divByZero));
        compiler.addInstruction(new REM(compiler.getRegister(number-1), compiler.getRegister(number - 2)));
        compiler.addInstruction(new BRA(fin));

        compiler.addLabel(divByZero);
        compiler.addInstruction(new WSTR("Erreur : division par 0"));
        compiler.addInstruction(new ERROR());

        compiler.addLabel(fin);
        compiler.libererRegistre(2);
    }


    @Override
    protected String getOperatorName() {
        return "%";
    }

}
