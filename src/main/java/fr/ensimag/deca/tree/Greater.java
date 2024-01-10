package fr.ensimag.deca.tree;


import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.instructions.BGT;
import fr.ensimag.ima.pseudocode.instructions.CMP;

/**
 * @author gl29
 * @date 01/01/2024
 */
public class Greater extends AbstractOpIneq {

    public Greater(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }

    @Override
    protected void codeGenInst(DecacCompiler compiler) {
        Label vrai = compiler.labelTable.addLabel("vrai_Greater");
        Label fin = compiler.labelTable.addLabel("fin_Greater");

        getLeftOperand().codeGenInst(compiler);
        getRightOperand().codeGenInst(compiler);

        compiler.libererRegistre(2);

        compiler.addInstruction(new CMP(compiler.getNextRegistreLibre(), compiler.getNextRegistreLibre()));
        compiler.addInstruction(new BGT(vrai));

        Equals.comparison(compiler, vrai, fin);
    }

    @Override
    protected String getOperatorName() {
        return ">";
    }

}
