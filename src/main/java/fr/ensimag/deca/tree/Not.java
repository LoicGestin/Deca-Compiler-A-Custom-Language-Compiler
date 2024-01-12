package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.codegen.codeGen;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.Type;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.instructions.BNE;
import fr.ensimag.ima.pseudocode.instructions.BRA;
import fr.ensimag.ima.pseudocode.instructions.CMP;
import fr.ensimag.ima.pseudocode.instructions.LOAD;


/**
 * @author gl29
 * @date 01/01/2024
 */
public class Not extends AbstractUnaryExpr {

    public Not(AbstractExpr operand) {
        super(operand);
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv,
                           ClassDefinition currentClass) throws ContextualError {
        getOperand().verifyCondition(compiler, localEnv, currentClass);
        setType(compiler.environmentType.BOOLEAN);

        return getType();
    }

    @Override
    protected void codeGenInst(DecacCompiler compiler) {
        Label vrai = compiler.labelTable.addLabel("vrai_Not");
        Label fin = compiler.labelTable.addLabel("fin_Not");

        getOperand().codeGenInst(compiler);
        compiler.addInstruction(new CMP(0, codeGen.getCurrentRegistreUtilise()));
        compiler.addInstruction(new BNE(vrai));

        to_rename_function(compiler, vrai, fin);
    }

    static void to_rename_function(DecacCompiler compiler, Label vrai, Label fin) {

        compiler.addInstruction(new LOAD(1, codeGen.getCurrentRegistreUtilise()));
        compiler.addInstruction(new BRA(fin));

        compiler.addLabel(vrai);
        compiler.addInstruction(new LOAD(0, codeGen.getCurrentRegistreUtilise()));

        compiler.addLabel(fin);
    }

    @Override
    protected String getOperatorName() {
        return "!";
    }
}
