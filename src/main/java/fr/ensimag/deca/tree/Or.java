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

        this.getLeftOperand().codeGenInst(compiler);
        compiler.libererRegistre();
        compiler.addInstruction(new CMP(1, compiler.getRegistreLibre()));
        compiler.addInstruction(new BEQ(vrai));

        this.getRightOperand().codeGenInst(compiler);
        compiler.libererRegistre();
        compiler.addInstruction(new CMP(1, compiler.getRegistreLibre()));
        compiler.addInstruction(new BEQ(vrai));
        compiler.addInstruction(new LOAD(0, compiler.getRegistreLibre()));
        compiler.addInstruction(new BRA(fin));
        compiler.addLabel(vrai);
        compiler.addInstruction(new LOAD(1, compiler.getRegistreLibre()));
        compiler.addLabel(fin);

    }

    @Override
    protected String getOperatorName() {
        return "||";
    }


}
