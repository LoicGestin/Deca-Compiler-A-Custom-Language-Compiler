package fr.ensimag.deca.tree;


import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.instructions.BLT;
import fr.ensimag.ima.pseudocode.instructions.BRA;
import fr.ensimag.ima.pseudocode.instructions.CMP;
import fr.ensimag.ima.pseudocode.instructions.LOAD;

/**
 * @author gl29
 * @date 01/01/2024
 */
public class Lower extends AbstractOpIneq {

    public Lower(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }

    @Override
    protected void codeGenInst(DecacCompiler compiler) {
        Label vrai = compiler.labelTable.addLabel("vrai_Lower");
        Label fin = compiler.labelTable.addLabel("fin_Lower");

        getLeftOperand().codeGenInst(compiler);
        getRightOperand().codeGenInst(compiler);

        compiler.libererRegistre(2);

        GPRegister r1 = compiler.getNextRegistreLibre();
        GPRegister r2 = compiler.getNextRegistreLibre();
        compiler.addInstruction(new CMP(r2, r1));
        compiler.addInstruction(new BLT(vrai));

        Equals.comparison(compiler, vrai, fin);
    }

    @Override
    protected String getOperatorName() {
        return "<";
    }

}
