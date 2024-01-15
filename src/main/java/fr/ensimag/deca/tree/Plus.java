package fr.ensimag.deca.tree;


import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.codegen.codeGen;
import fr.ensimag.ima.pseudocode.instructions.ADD;
import fr.ensimag.ima.pseudocode.instructions.BOV;

/**
 * @author gl29
 * @date 01/01/2024
 */
public class Plus extends AbstractOpArith {
    public Plus(AbstractExpr leftOperand, AbstractExpr rightOperand) {
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
        compiler.addInstruction(new ADD(codeGen.getRegistreCourant(compiler), codeGen.getCurrentRegistreUtilise()));
        if(!DecacCompiler.getNocheck()) {
            compiler.addInstruction(new BOV(compiler.getOverflow_error()));
        }
    }


    @Override
    protected String getOperatorName() {
        return "+";
    }
}
