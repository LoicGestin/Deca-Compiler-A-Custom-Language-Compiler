package fr.ensimag.deca.tree;


import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.Type;
import fr.ensimag.ima.pseudocode.instructions.SUB;

/**
 * @author gl29
 * @date 01/01/2024
 */
public class Minus extends AbstractOpArith {
    public Minus(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }

    @Override
    public void codeGenInst(DecacCompiler compiler) {
        AbstractExpr LValue = this.getLeftOperand();
        AbstractExpr RValue = this.getRightOperand();
        Type tR = RValue.getType();
        Type tL = LValue.getType();

        if (!(tR.isFloat() || tR.isInt()) && !(tL.isFloat() || tL.isInt())) {
            try {
                throw new ContextualError("Exception : Incompatible for minus : " + tR + " and " + tL, RValue.getLocation());
            } catch (ContextualError e) {
                throw new RuntimeException(e);
            }
        } else {
            LValue.codeGenInst(compiler);
            RValue.codeGenInst(compiler);
            int number = compiler.getNextRegistreLibre().getNumber();
            compiler.addInstruction(new SUB(compiler.getRegister(number - 1), compiler.getRegister(number - 2)));
            compiler.libererRegistre(2);
        }
    }


    @Override
    protected String getOperatorName() {
        return "-";
    }

}
