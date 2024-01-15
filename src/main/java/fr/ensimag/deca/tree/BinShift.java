package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.codegen.codeGen;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.Type;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.instructions.*;

public class BinShift extends AbstractBinaryExpr {

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

        if (!typeR.isInt()){
            throw new ContextualError("Exception : binary shift value must be int, is " + typeR, getLocation());
        }
        else if (!typeL.isInt()){
            throw new ContextualError("Exception : binary shift can only be applied to int not " + typeL, getLocation());
        }

        setType(typeL);
        return getType();
    }

    @Override
    public void codeGenInst(DecacCompiler compiler) {
        AbstractExpr LValue = this.getLeftOperand();
        AbstractExpr RValue = this.getRightOperand();

        Label debut_bs = compiler.labelTable.addLabel("debut_bs");
        Label fin_bs = compiler.labelTable.addLabel("fin_bs");

        codeGen.setAssignation(true);
        LValue.codeGenInst(compiler);
        codeGen.setAssignation(false);
        RValue.codeGenInst(compiler);

        compiler.addLabel(debut_bs);

        compiler.addInstruction(new CMP(0, codeGen.getCurrentRegistreUtilise()));
        compiler.addInstruction(new BEQ(fin_bs));

        compiler.addInstruction(new SNE(codeGen.getRegistreLibre()));
        compiler.addInstruction(new SUB(codeGen.getRegistreUtilise(), codeGen.getRegistreUtilise()));

        if (direction == 0){
            compiler.addInstruction(new SHL(codeGen.getCurrentRegistreUtilise()));

        } else {
            compiler.addInstruction((new SHR(codeGen.getCurrentRegistreUtilise())));
        }

        compiler.addInstruction(new BRA(debut_bs));
        compiler.addLabel(fin_bs);

        if (!DecacCompiler.getNocheck()) {
            compiler.addInstruction(new BOV(compiler.getOverflow_error()));
        }

    }

    @Override
    protected String getOperatorName() {
        if (direction == 0) return "<<";
        else return ">>";
    }
}

