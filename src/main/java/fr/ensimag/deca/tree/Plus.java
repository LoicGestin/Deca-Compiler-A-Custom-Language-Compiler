package fr.ensimag.deca.tree;


import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.ima.pseudocode.instructions.ADD;

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

        LValue.codeGenInst(compiler);
        RValue.codeGenInst(compiler);
        int number = compiler.getNextRegistreLibre().getNumber();
        compiler.addInstruction(new ADD(compiler.getRegister(number - 1), compiler.getRegister(number - 2)));
        compiler.libererRegistre(3);
    }


    @Override
    protected String getOperatorName() {
        return "+";
    }
}
