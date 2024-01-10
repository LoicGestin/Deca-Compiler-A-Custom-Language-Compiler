package fr.ensimag.deca.tree;


import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.Type;
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
public class Equals extends AbstractOpExactCmp {

    public Equals(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }

    @Override
    protected void verifyInst(DecacCompiler compiler, EnvironmentExp localEnv, ClassDefinition currentClass, Type returnType) throws ContextualError {
        if(!this.getLeftOperand().getType().isBoolean() || !this.getRightOperand().getType().isBoolean()){
            throw new ContextualError("Boolean expected", this.getLocation());
        }
    }

    @Override
    protected void codeGenInst(DecacCompiler compiler) {
        Label vrai = compiler.labelTable.create("vrai_Equals");
        Label fin = compiler.labelTable.create("fin_Equals");

        getLeftOperand().codeGenInst(compiler);
        getRightOperand().codeGenInst(compiler);

        compiler.libererRegistre();
        compiler.libererRegistre();

        compiler.addInstruction(new CMP(compiler.getNextRegistreLibre(), compiler.getNextRegistreLibre()));
        compiler.addInstruction(new BEQ(vrai));

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
        return "==";
    }    
    
}
