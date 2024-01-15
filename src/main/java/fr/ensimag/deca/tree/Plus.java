package fr.ensimag.deca.tree;


import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.codegen.codeGen;
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
    public void codeGenOperator(DecacCompiler compiler) {
        compiler.addInstruction(new ADD(codeGen.getRegistreCourant(compiler), codeGen.getCurrentRegistreUtilise()));
    }


    @Override
    protected String getOperatorName() {
        return "+";
    }
}
