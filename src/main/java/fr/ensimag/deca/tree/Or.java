package fr.ensimag.deca.tree;


import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.codegen.codeGen;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.instructions.BEQ;
import fr.ensimag.ima.pseudocode.instructions.BRA;
import fr.ensimag.ima.pseudocode.instructions.CMP;
import fr.ensimag.ima.pseudocode.instructions.LOAD;

/**
 * @author gl29
 * @date 01/01/2024
 */
public class Or extends AbstractOpBool {

    public Or(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }

    static void condition_branch(DecacCompiler compiler, Label vrai, Label fin) {
        compiler.addInstruction(new LOAD(0, codeGen.getRegistreLibre()));
        compiler.addInstruction(new BRA(fin));

        compiler.addLabel(vrai);
        compiler.addInstruction(new LOAD(1, codeGen.getCurrentRegistreUtilise()));

        compiler.addLabel(fin);
    }

    @Override
    protected void codeGenInst(DecacCompiler compiler) {
        Label vrai = compiler.labelTable.addLabel("vrai_Or");
        Label fin = compiler.labelTable.addLabel("fin_Or");

        getLeftOperand().codeGenInst(compiler);
        compiler.addInstruction(new CMP(1, codeGen.getRegistreUtilise()));
        compiler.addInstruction(new BEQ(vrai));
        getRightOperand().codeGenInst(compiler);
        compiler.addInstruction(new CMP(1, codeGen.getRegistreUtilise()));
        compiler.addInstruction(new BEQ(vrai));
        condition_branch(compiler, vrai, fin);
    }

    @Override
    protected String getOperatorName() {
        return "||";
    }


}
