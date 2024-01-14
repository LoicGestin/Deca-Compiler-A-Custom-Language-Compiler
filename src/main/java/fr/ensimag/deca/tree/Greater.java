package fr.ensimag.deca.tree;


import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.codegen.codeGen;
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
        codeGen.setAssignation(true);
        getLeftOperand().codeGenInst(compiler);
        codeGen.setAssignation(true);
        getRightOperand().codeGenInst(compiler);

        compiler.addInstruction(new CMP(codeGen.getRegistreUtilise(), codeGen.getRegistreUtilise()));
        compiler.addInstruction(new BGT(vrai));

        Equals.comparison(compiler, vrai, fin);
    }

    @Override
    protected String getOperatorName() {
        return ">";
    }

}
