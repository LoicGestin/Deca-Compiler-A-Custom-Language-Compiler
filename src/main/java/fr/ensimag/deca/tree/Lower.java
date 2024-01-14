package fr.ensimag.deca.tree;


import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.codegen.codeGen;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.instructions.BLT;
import fr.ensimag.ima.pseudocode.instructions.CMP;

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

        codeGen.setAssignation(true);
        getLeftOperand().codeGenInst(compiler);
        codeGen.setAssignation(true);
        getRightOperand().codeGenInst(compiler);

        compiler.addInstruction(new CMP(codeGen.getRegistreUtilise(), codeGen.getRegistreUtilise()));
        compiler.addInstruction(new BLT(vrai));

        Equals.comparison(compiler, vrai, fin);
    }

    @Override
    protected String getOperatorName() {
        return "<";
    }

}
