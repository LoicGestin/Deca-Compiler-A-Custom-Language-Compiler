package fr.ensimag.deca.tree;


import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.codegen.codeGen;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.Type;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.instructions.BEQ;
import fr.ensimag.ima.pseudocode.instructions.BRA;
import fr.ensimag.ima.pseudocode.instructions.CMP;
import fr.ensimag.ima.pseudocode.instructions.LOAD;

/**
 * @author gl29
 * @date 01/01/2024
 */
public class Equals extends AbstractOpExactCmp {

    public Equals(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }

    @Override
    protected void verifyInst(DecacCompiler compiler, EnvironmentExp localEnv, ClassDefinition currentClass, Type returnType) throws ContextualError {
        if (!this.getLeftOperand().getType().isBoolean() || !this.getRightOperand().getType().isBoolean()) {
            throw new ContextualError("Boolean expected", this.getLocation());
        }
    }

    @Override
    protected void codeGenInst(DecacCompiler compiler) {
        Label vrai = compiler.labelTable.addLabel("vrai_Equals");
        Label fin = compiler.labelTable.addLabel("fin_Equals");

        getLeftOperand().codeGenInst(compiler);
        getRightOperand().codeGenInst(compiler);

        compiler.addInstruction(new CMP(codeGen.getRegistreUtilise(), codeGen.getCurrentRegistreUtilise()));
        compiler.addInstruction(new BEQ(vrai));
        comparison(compiler, vrai, fin);

    }

    static void comparison(DecacCompiler compiler, Label vrai, Label fin) {
        Or.condition_branch(compiler, vrai, fin);
    }

    @Override
    protected String getOperatorName() {
        return "==";
    }

}
