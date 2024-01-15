package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.codegen.codeGen;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.Type;
import fr.ensimag.ima.pseudocode.instructions.LOAD;
import fr.ensimag.ima.pseudocode.instructions.SUB;
import fr.ensimag.ima.pseudocode.Register;


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
    public void codeGenOp(DecacCompiler compiler) {
        // We do 1 - the value of the operand
        compiler.addInstruction(new LOAD(1, Register.getR(1)));
        compiler.addInstruction(new SUB(Register.getR(1), codeGen.getCurrentRegistreUtilise()));
    }

    @Override
    protected String getOperatorName() {
        return "!";
    }
}
