package fr.ensimag.deca.tree;


import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.codegen.codeGen;
import fr.ensimag.ima.pseudocode.instructions.BOV;
import fr.ensimag.ima.pseudocode.instructions.DIV;
import fr.ensimag.ima.pseudocode.instructions.QUO;

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
        codeGen.setAssignation(true);
        LValue.codeGenInst(compiler);
        codeGen.setAssignation(false);
        RValue.codeGenInst(compiler);


        if (LValue.getType().isInt() && RValue.getType().isInt()) {
            compiler.addInstruction(new QUO(codeGen.getRegistreCourant(compiler), codeGen.getCurrentRegistreUtilise()));
            if (!DecacCompiler.getNocheck()) {
                compiler.addInstruction(new BOV(compiler.getOverflow_error()));
            }
        } else {
            compiler.addInstruction(new DIV(codeGen.getRegistreCourant(compiler), codeGen.getCurrentRegistreUtilise()));
            if (!DecacCompiler.getNocheck()) {
                compiler.addInstruction(new BOV(compiler.getOverflow_error()));
            }
        }
    }


    @Override
    protected String getOperatorName() {
        return "/";
    }

}
