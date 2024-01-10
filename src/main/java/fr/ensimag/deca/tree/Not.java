package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.instructions.BNE;
import fr.ensimag.ima.pseudocode.instructions.BRA;
import fr.ensimag.ima.pseudocode.instructions.CMP;
import fr.ensimag.ima.pseudocode.instructions.LOAD;
import sun.management.spi.PlatformMBeanProvider;

/**
 *
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
        Label vrai = new Label("vrai");
        Label fin = new Label("fin");

        getOperand().codeGenInst(compiler);

        compiler.libererRegistre();

        compiler.addInstruction(new CMP(0, compiler.getRegistreLibre()));
        compiler.addInstruction(new BNE(vrai));

        compiler.libererRegistre();

        compiler.addInstruction(new LOAD(1, compiler.getRegistreLibre()));
        compiler.addInstruction(new BRA(fin));

        compiler.addLabel(vrai);
        compiler.addInstruction(new LOAD(0, compiler.getRegistreLibre()));

        compiler.addLabel(fin);
    }

    @Override
    protected String getOperatorName() {
        return "!";
    }
}
