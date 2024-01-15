package fr.ensimag.deca.tree;


import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.codegen.codeGen;
import fr.ensimag.ima.pseudocode.instructions.ADD;
import fr.ensimag.ima.pseudocode.instructions.CMP;
import fr.ensimag.ima.pseudocode.instructions.SGE;

/**
 * @author gl29
 * @date 01/01/2024
 */
public class Or extends AbstractOpBool {

    public Or(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }

    @Override
    public void codeGenOp(DecacCompiler compiler) {
        // Add the value and compare it to 2
        compiler.addInstruction(new ADD(codeGen.getRegistreCourant(compiler), codeGen.getCurrentRegistreUtilise()));
        compiler.addInstruction(new CMP(1, codeGen.getCurrentRegistreUtilise()));
        // If the value is 2, then it's true
        compiler.addInstruction(new SGE(codeGen.getCurrentRegistreUtilise()));
    }

    @Override
    protected String getOperatorName() {
        return "||";
    }


}
