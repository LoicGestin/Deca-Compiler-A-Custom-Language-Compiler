package fr.ensimag.deca.tree;


import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.instructions.BLE;
import fr.ensimag.ima.pseudocode.instructions.BRA;
import fr.ensimag.ima.pseudocode.instructions.CMP;
import fr.ensimag.ima.pseudocode.instructions.LOAD;

/**
 *
 * @author gl29
 * @date 01/01/2024
 */
public class LowerOrEqual extends AbstractOpIneq {
    public LowerOrEqual(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }

    @Override
    protected void codeGenInst(DecacCompiler compiler) {
        Label vrai = compiler.labelTable.addLabel("vrai_LowerOrEqual");
        Label fin = compiler.labelTable.addLabel("fin_LowerOrEqual");

        getLeftOperand().codeGenInst(compiler);
        getRightOperand().codeGenInst(compiler);

        compiler.libererRegistre();
        compiler.libererRegistre();

        compiler.addInstruction(new CMP(compiler.getNextRegistreLibre(), compiler.getNextRegistreLibre()));
        compiler.addInstruction(new BLE(vrai));

        compiler.libererRegistre();
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
        return "<=";
    }

}
