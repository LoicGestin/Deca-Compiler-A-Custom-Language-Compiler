package fr.ensimag.deca.tree;


import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.codegen.codeGen;
import fr.ensimag.ima.pseudocode.instructions.BOV;
import fr.ensimag.ima.pseudocode.instructions.MUL;

/**
 * @author gl29
 * @date 01/01/2024
 */
public class Multiply extends AbstractOpArith {
    public Multiply(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }

    @Override
    public void codeGenInst(DecacCompiler compiler) {
        AbstractExpr LValue = this.getLeftOperand();
        AbstractExpr RValue = this.getRightOperand();
        LValue.codeGenInst(compiler);
        RValue.codeGenInst(compiler);
        compiler.addInstruction(new MUL(codeGen.getRegistreUtilise(), codeGen.getCurrentRegistreUtilise()));
        if(!DecacCompiler.getNocheck()) {
            compiler.addInstruction(new BOV(compiler.getOverflow_error()));
        }
    }


    @Override
    protected String getOperatorName() {
        return "*";
    }

}
