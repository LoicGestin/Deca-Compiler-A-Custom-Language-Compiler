package fr.ensimag.deca.tree;


import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.codegen.codeGen;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.Type;
import fr.ensimag.ima.pseudocode.instructions.BOV;
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
            codeGen.setAssignation(true);
            LValue.codeGenInst(compiler);
            codeGen.setAssignation(false);
            RValue.codeGenInst(compiler);
            compiler.addInstruction(new SUB(codeGen.getRegistreCourant(compiler), codeGen.getCurrentRegistreUtilise()));
            if (!DecacCompiler.getNocheck()) {
                compiler.addInstruction(new BOV(compiler.getOverflow_error()));
            }
        }
    }


    @Override
    protected String getOperatorName() {
        return "-";
    }

}
