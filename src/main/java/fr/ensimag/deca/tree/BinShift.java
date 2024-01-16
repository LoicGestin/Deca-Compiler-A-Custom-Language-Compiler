package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.codegen.codeGen;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.Type;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.ImmediateInteger;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.instructions.*;

public class BinShift extends AbstractOpArith {

    //0 = LEFT; 1 = RIGHT
    private final int direction;

    public BinShift(AbstractExpr leftOp, AbstractExpr rightOp, int direction) {
        super(leftOp, rightOp);
        this.direction = direction;
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv,
                           ClassDefinition currentClass) throws ContextualError {
        Type typeL = getLeftOperand().verifyExpr(compiler, localEnv, currentClass);
        Type typeR = getRightOperand().verifyExpr(compiler, localEnv, currentClass);

        if (!typeR.isInt()) {
            throw new ContextualError("Exception : binary shift value must be int, is " + typeR, getLocation());
        } else if (!typeL.isInt()) {
            throw new ContextualError("Exception : binary shift can only be applied to int not " + typeL, getLocation());
        }

        setType(typeL);
        return getType();
    }

    @Override
    public void codeGenInst(DecacCompiler compiler) {
        codeGen.setAssignation(true);
        this.getLeftOperand().codeGenInst(compiler);
        codeGen.setAssignation(true);
        this.getRightOperand().codeGenInst(compiler);

        codeGenOperator(compiler);

        if (!DecacCompiler.getNocheck()) {
            compiler.addInstruction(new BOV(compiler.getOverflow_error()));
        }
    }

    @Override
    public void codeGenOperator(DecacCompiler compiler) {
        // Create a loop that will shift the left operand by the right operand
        Label debut_bs = compiler.labelTable.addLabel("debut_bs");
        Label fin_bs = compiler.labelTable.addLabel("fin_bs");

        GPRegister rgauche =  codeGen.getRegistreUtilise();        // 2
        GPRegister rdroite =  codeGen.getCurrentRegistreUtilise(); // 8

        compiler.addLabel(debut_bs);
        // 8 << 2

        // If the right operand is 0, we stop the loop
        compiler.addInstruction(new CMP(0, rgauche));
        compiler.addInstruction(new BEQ(fin_bs));

        // else we shift the left operand by 1
        if (direction == 0) {
            compiler.addInstruction(new SHL(rdroite));
        } else {
            compiler.addInstruction(new SHR(rdroite));
        }

        // We decrement the right operand by 1
        compiler.addInstruction(new SUB(new ImmediateInteger(1), rgauche));
        compiler.addInstruction(new BRA(debut_bs));

        compiler.addLabel(fin_bs);

    }

    @Override
    protected String getOperatorName() {
        if (direction == 0) return "<<";
        else return ">>";
    }
}

