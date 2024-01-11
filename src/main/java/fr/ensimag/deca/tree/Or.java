package fr.ensimag.deca.tree;


import fr.ensimag.deca.DecacCompiler;
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

    @Override
    protected void codeGenInst(DecacCompiler compiler) {
        Label vrai = compiler.labelTable.addLabel("vrai_Or");
        Label fin = compiler.labelTable.addLabel("fin_Or");

        getLeftOperand().codeGenInst(compiler);
        compiler.libererRegistre();

        compiler.addInstruction(new CMP(1, compiler.getNextRegistreLibre()));
        compiler.addInstruction(new BEQ(vrai));
        compiler.libererRegistre();

        getRightOperand().codeGenInst(compiler);
        compiler.libererRegistre();
        compiler.addInstruction(new CMP(1, compiler.getNextRegistreLibre()));
        compiler.addInstruction(new BEQ(vrai));
        compiler.libererRegistre();

        compiler.addInstruction(new LOAD(0, compiler.getNextRegistreLibre()));
        compiler.addInstruction(new BRA(fin));
        compiler.libererRegistre();

        compiler.addLabel(vrai);
        compiler.addInstruction(new LOAD(1, compiler.getNextRegistreLibre()));

        compiler.addLabel(fin);
    }

    @Override
    protected String getOperatorName() {
        return "||";
    }


}
