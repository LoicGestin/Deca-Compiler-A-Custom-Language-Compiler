package fr.ensimag.deca.tree;


import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.codegen.codeGen;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.instructions.BNE;
import fr.ensimag.ima.pseudocode.instructions.BRA;
import fr.ensimag.ima.pseudocode.instructions.CMP;
import fr.ensimag.ima.pseudocode.instructions.LOAD;

/**
 * @author gl29
 * @date 01/01/2024
 */
public class NotEquals extends AbstractOpExactCmp {

    public NotEquals(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }

    @Override
    protected void codeGenInst(DecacCompiler compiler) {
        Label vrai = compiler.labelTable.addLabel("vrai_NotEquals");
        Label fin = compiler.labelTable.addLabel("fin_NotEquals");

        getLeftOperand().codeGenInst(compiler);
        getRightOperand().codeGenInst(compiler);


        compiler.addInstruction(new CMP(codeGen.getRegistreUtilise(), codeGen.getRegistreUtilise()));
        compiler.addInstruction(new BNE(vrai));


        compiler.addInstruction(new LOAD(0, codeGen.getRegistreLibre()));
        compiler.addInstruction(new BRA(fin));


        compiler.addLabel(vrai);
        compiler.addInstruction(new LOAD(1, codeGen.getCurrentRegistreUtilise()));

        compiler.addLabel(fin);
    }

    @Override
    protected String getOperatorName() {
        return "!=";
    }

}
