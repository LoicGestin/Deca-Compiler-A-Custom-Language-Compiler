package fr.ensimag.deca.tree;


import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.codegen.codeGen;
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
    public void codeGenOperator(DecacCompiler compiler) {
        if (getLeftOperand().getType().isInt() && getRightOperand().getType().isInt()) {
            compiler.addInstruction(new QUO(codeGen.getRegistreCourant(compiler), codeGen.getCurrentRegistreUtilise()));
        } else {
            compiler.addInstruction(new DIV(codeGen.getRegistreCourant(compiler), codeGen.getCurrentRegistreUtilise()));
        }
    }


    @Override
    protected String getOperatorName() {
        return "/";
    }

}
