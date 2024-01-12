package fr.ensimag.deca.tree;


import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.codegen.codeGen;
import fr.ensimag.ima.pseudocode.ImmediateFloat;
import fr.ensimag.ima.pseudocode.ImmediateInteger;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.instructions.*;

/**
 * @author gl29
 * @date 01/01/2024
 */
public class Divide extends AbstractOpArith {
    public Divide(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }

    @Override
    public void codeGenInst(DecacCompiler compiler) {
        AbstractExpr LValue = this.getLeftOperand();
        AbstractExpr RValue = this.getRightOperand();
        Label divByZero = compiler.labelTable.addLabel("divByZero");
        Label fin = compiler.labelTable.addLabel("fin");

        LValue.codeGenInst(compiler);
        RValue.codeGenInst(compiler);


        if (LValue.getType().isInt() && RValue.getType().isInt()) {
            compiler.addInstruction(new QUO(codeGen.getRegistreUtilise(), codeGen.getCurrentRegistreUtilise()));
            compiler.addInstruction(new BOV(compiler.getOverflow_error()));
            compiler.addInstruction(new BRA(fin));
        } else {
            compiler.addInstruction(new DIV(codeGen.getRegistreUtilise(), codeGen.getCurrentRegistreUtilise()));
            compiler.addInstruction(new BOV(compiler.getOverflow_error()));
            compiler.addInstruction(new BRA(fin));
        }

        compiler.addLabel(fin);
    }


    @Override
    protected String getOperatorName() {
        return "/";
    }

}
