package fr.ensimag.deca.tree;


import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.instructions.BEQ;
import fr.ensimag.ima.pseudocode.instructions.BRA;
import fr.ensimag.ima.pseudocode.instructions.CMP;
import fr.ensimag.ima.pseudocode.instructions.LOAD;

/**
 *
 * @author gl29
 * @date 01/01/2024
 */
public class And extends AbstractOpBool {

    public And(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }

    @Override
    protected void codeGenInst(DecacCompiler compiler) {
        Label faux = compiler.labelTable.addLabel("faux_And");
        Label fin = compiler.labelTable.addLabel("fin_And");

        getLeftOperand().codeGenInst(compiler);
        compiler.libererRegistre();
        compiler.addInstruction(new CMP(0, compiler.getRegistreLibre()));
        compiler.addInstruction(new BEQ(faux));
        compiler.libererRegistre();

        getRightOperand().codeGenInst(compiler);
        compiler.libererRegistre();
        compiler.addInstruction(new CMP(0, compiler.getRegistreLibre()));
        compiler.addInstruction(new BEQ(faux));
        compiler.libererRegistre();

        compiler.addInstruction(new LOAD(1, compiler.getRegistreLibre()));
        compiler.addInstruction(new BRA(fin));

        compiler.libererRegistre();

        compiler.addLabel(faux);
        compiler.addInstruction(new LOAD(0, compiler.getRegistreLibre()));

        compiler.addLabel(fin);
    }
    @Override
    protected String getOperatorName() {
        return "&&";
    }


}
