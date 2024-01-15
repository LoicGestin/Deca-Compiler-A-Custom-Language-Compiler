package fr.ensimag.deca.tree;


import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.codegen.codeGen;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.instructions.BGE;
import fr.ensimag.ima.pseudocode.instructions.CMP;

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

        codeGen.setAssignation(true);
        getLeftOperand().codeGenInst(compiler);
        codeGen.setAssignation(false);
        getRightOperand().codeGenInst(compiler);

        compiler.addInstruction(new CMP(codeGen.getRegistreCourant(compiler), codeGen.getRegistreUtilise()));
        compiler.addInstruction(new BGE(vrai));

        Equals.comparison(compiler, vrai, fin);
    }

    @Override
    protected String getOperatorName() {
        return ">=";
    }

}
