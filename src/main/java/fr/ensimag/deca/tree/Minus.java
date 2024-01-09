package fr.ensimag.deca.tree;


import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.Type;
import fr.ensimag.ima.pseudocode.instructions.ADD;
import fr.ensimag.ima.pseudocode.instructions.LOAD;
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
    public void codeGenArith(DecacCompiler compiler) {
        AbstractExpr LValue = this.getLeftOperand();
        AbstractExpr RValue = this.getRightOperand();
        Type tR = RValue.getType();
        Type tL = LValue.getType();

        if (!(tR.isFloat() || tR.isInt()) && !(tL.isFloat() || tL.isInt())) {
            try {
                throw new ContextualError("Incompatible types in initialization", RValue.getLocation());
            } catch (ContextualError e) {
                throw new RuntimeException(e);
            }
        }

        else {
            LValue.codeGenInst(compiler);
            RValue.codeGenInst(compiler);
            compiler.addInstruction(new SUB(compiler.getRegister(3), compiler.getRegister(2)));
            compiler.addInstruction(new LOAD(compiler.getRegister(2), compiler.getNextRegistreLibre()));
            compiler.libererRegistre();
            compiler.libererRegistre();
        }
    }


    @Override
    protected String getOperatorName() {
        return "-";
    }
    
}
