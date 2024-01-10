package fr.ensimag.deca.tree;


import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.instructions.*;

/**
 *
 * @author gl29
 * @date 01/01/2024
 */
public class Divide extends AbstractOpArith {
    public Divide(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }

    @Override
    public void codeGenArith(DecacCompiler compiler) {
        AbstractExpr LValue = this.getLeftOperand();
        AbstractExpr RValue = this.getRightOperand();
        Label divByZero = new Label("divByZero");

        LValue.codeGenInst(compiler);
        RValue.codeGenInst(compiler);

        compiler.getRegistreLibre();
        compiler.addInstruction(new CMP(0, compiler.getRegistreLibre()));
        compiler.addInstruction(new BEQ(divByZero));

        int number = compiler.getNextRegistreLibre().getNumber();
        
        if (LValue.getType().isInt() && RValue.getType().isInt()) {
            compiler.addInstruction(new QUO(compiler.getRegister(number-1), compiler.getRegister(number-2)));
        }

        else {
            compiler.addInstruction(new DIV(compiler.getRegister(number-1), compiler.getRegister(number-2)));
        }

        compiler.addLabel(divByZero);
        compiler.addInstruction(new WSTR("Erreur : division par 0"));

        compiler.libererRegistre();
        compiler.libererRegistre();
    }


    @Override
    protected String getOperatorName() {
        return "/";
    }

}
