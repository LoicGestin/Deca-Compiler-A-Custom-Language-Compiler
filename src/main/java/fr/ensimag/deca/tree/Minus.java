package fr.ensimag.deca.tree;


import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.codegen.codeGen;
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
    public void codeGenOperator(DecacCompiler compiler) {
        compiler.addInstruction(new SUB(codeGen.getRegistreCourant(compiler), codeGen.getCurrentRegistreUtilise()));
    }


    @Override
    protected String getOperatorName() {
        return "-";
    }

}
