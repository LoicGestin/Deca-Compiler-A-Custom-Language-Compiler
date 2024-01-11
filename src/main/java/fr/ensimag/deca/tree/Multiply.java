package fr.ensimag.deca.tree;


import fr.ensimag.deca.DecacCompiler;
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
        int number = compiler.getNextRegistreLibre().getNumber();
        compiler.addInstruction(new MUL(compiler.getRegister(number - 1), compiler.getRegister(number - 2)));
        compiler.libererRegistre(2);
    }


    @Override
    protected String getOperatorName() {
        return "*";
    }

}
