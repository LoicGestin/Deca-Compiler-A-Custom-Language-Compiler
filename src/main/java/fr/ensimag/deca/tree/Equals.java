package fr.ensimag.deca.tree;


import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.codegen.codeGen;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.Type;
import fr.ensimag.ima.pseudocode.instructions.CMP;
import fr.ensimag.ima.pseudocode.instructions.SEQ;

/**
 * @author gl29
 * @date 01/01/2024
 */
public class Equals extends AbstractOpExactCmp {

    public Equals(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }
    @Override
    public void codeGenOp(DecacCompiler compiler) {
        compiler.addInstruction(new CMP(codeGen.getRegistreCourant(compiler), codeGen.getRegistreUtilise()));
        compiler.addInstruction(new SEQ(codeGen.getRegistreLibre()));
    }

    @Override
    protected String getOperatorName() {
        return "==";
    }

}
