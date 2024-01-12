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

        LValue.codeGenInst(compiler);
        RValue.codeGenInst(compiler);


        if (LValue.getType().isInt() && RValue.getType().isInt()) {
            compiler.addInstruction(new QUO(codeGen.getRegistreUtilise(), codeGen.getCurrentRegistreUtilise()));
            if(!DecacCompiler.getNocheck()) {
                compiler.addInstruction(new BOV(compiler.getOverflow_error()));
            }
        } else {
            compiler.addInstruction(new DIV(codeGen.getRegistreUtilise(), codeGen.getCurrentRegistreUtilise()));
            if(!DecacCompiler.getNocheck()) {
                compiler.addInstruction(new BOV(compiler.getOverflow_error()));
            }
        }
    }


    @Override
    protected String getOperatorName() {
        return "/";
    }

}
