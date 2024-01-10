package fr.ensimag.deca.tree;


import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.instructions.BNE;
import fr.ensimag.ima.pseudocode.instructions.BRA;
import fr.ensimag.ima.pseudocode.instructions.CMP;
import fr.ensimag.ima.pseudocode.instructions.LOAD;

/**
 *
 * @author gl29
 * @date 01/01/2024
 */
public class NotEquals extends AbstractOpExactCmp {

    public NotEquals(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }

    @Override
    protected void codeGenInst(DecacCompiler compiler) {
        Label vrai = new Label("vrai");
        Label fin = new Label("fin");

        getLeftOperand().codeGenInst(compiler);
        getRightOperand().codeGenInst(compiler);

        compiler.libererRegistre();
        compiler.libererRegistre();

        compiler.addInstruction(new CMP(compiler.getNextRegistreLibre(), compiler.getNextRegistreLibre()));
        compiler.addInstruction(new BNE(vrai));

        compiler.libererRegistre();
        compiler.libererRegistre();

        compiler.addInstruction(new LOAD(0, compiler.getNextRegistreLibre()));
        compiler.addInstruction(new BRA(fin));

        compiler.libererRegistre();

        compiler.addLabel(vrai);
        compiler.addInstruction(new LOAD(1, compiler.getRegistreLibre()));

        compiler.addLabel(fin);
    }
    @Override
    protected String getOperatorName() {
        return "!=";
    }

}
