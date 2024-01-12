package fr.ensimag.deca.tree;


import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.codegen.codeGen;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.instructions.BEQ;
import fr.ensimag.ima.pseudocode.instructions.CMP;

/**
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
        compiler.addInstruction(new CMP(0, codeGen.getRegistreUtilise()));
        compiler.addInstruction(new BEQ(faux));
        getRightOperand().codeGenInst(compiler);
        compiler.addInstruction(new CMP(0,codeGen.getCurrentRegistreUtilise()));
        compiler.addInstruction(new BEQ(faux));
        Not.to_rename_function(compiler, faux, fin);
    }

    @Override
    protected String getOperatorName() {
        return "&&";
    }


}
