package fr.ensimag.deca.tree;


import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.Type;
import fr.ensimag.ima.pseudocode.ImmediateFloat;
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
    public void codeGenArith(DecacCompiler compiler) throws ContextualError {
        AbstractExpr LValue = this.getLeftOperand();
        AbstractExpr RValue = this.getRightOperand();
        Type tR = RValue.getType();
        Type tL = LValue.getType();

        if (!(tR.isFloat() || tR.isInt()) && !(tL.isFloat() || tL.isInt())) {
            throw new ContextualError("Incompatible types in initialization", RValue.getLocation());
        }

        else {
            if (tR.isFloat()) {
                FloatLiteral fR = (FloatLiteral) RValue;
                LValue.codeGenInst(compiler);
                compiler.addInstruction(new ADD(new ImmediateFloat(fR.getValue()), compiler.getRegister(2)));
            }

            else {
                IntLiteral fR = (IntLiteral) RValue;
                LValue.codeGenInst(compiler);
                compiler.addInstruction(new ADD(new ImmediateFloat(fR.getValue()), compiler.getRegister(2)));
            }
        }
    }


    @Override
    protected String getOperatorName() {
        return "+";
    }
}
