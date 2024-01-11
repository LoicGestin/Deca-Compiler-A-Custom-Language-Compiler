package fr.ensimag.deca.tree;


import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.instructions.BGE;
import fr.ensimag.ima.pseudocode.instructions.BRA;
import fr.ensimag.ima.pseudocode.instructions.CMP;
import fr.ensimag.ima.pseudocode.instructions.LOAD;

/**
 * Operator "x >= y"
 *
 * @author gl29
 * @date 01/01/2024
 */
public class GreaterOrEqual extends AbstractOpIneq {

    public GreaterOrEqual(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }

    @Override
    protected void codeGenInst(DecacCompiler compiler) {
        Label vrai = compiler.labelTable.addLabel("vrai_GreaterOrEqual");
        Label fin = compiler.labelTable.addLabel("fin_GreaterOrEqual");

        getLeftOperand().codeGenInst(compiler);
        getRightOperand().codeGenInst(compiler);

        compiler.libererRegistre(2);

        GPRegister r1 = compiler.getNextRegistreLibre();
        GPRegister r2 = compiler.getNextRegistreLibre();
        compiler.addInstruction(new CMP(r2, r1));
        compiler.addInstruction(new BGE(vrai));

        Equals.comparison(compiler, vrai, fin);
    }

    @Override
    protected String getOperatorName() {
        return ">=";
    }

}
