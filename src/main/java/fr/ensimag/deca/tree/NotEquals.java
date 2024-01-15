package fr.ensimag.deca.tree;


import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.codegen.codeGen;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.instructions.*;

/**
 * @author gl29
 * @date 01/01/2024
 */
public class NotEquals extends AbstractOpExactCmp {

    public NotEquals(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }

    @Override
    public void codeGenOp(DecacCompiler compiler) {
        compiler.addInstruction(new CMP(codeGen.getRegistreCourant(compiler), codeGen.getRegistreUtilise()));
        compiler.addInstruction(new SNE(codeGen.getRegistreLibre()));
    }

    @Override
    protected String getOperatorName() {
        return "!=";
    }

}
