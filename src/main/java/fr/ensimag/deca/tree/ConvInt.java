package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.codegen.codeGen;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.Type;
import fr.ensimag.ima.pseudocode.instructions.INT;

public class ConvInt extends AbstractUnaryExpr {
    public ConvInt(AbstractExpr operand) {
        super(operand);
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv, ClassDefinition currentClass) throws ContextualError {

        if (!getOperand().getType().isFloat()) {
            throw new ContextualError("Exception : Conversion vers un entier d'un non flottant", getLocation());
        }

        setType(compiler.environmentType.INT);
        return getType();
    }

    @Override
    public void codeGenOp(DecacCompiler compiler) {
        compiler.addInstruction(new INT(codeGen.getCurrentRegistreUtilise(), codeGen.getCurrentRegistreUtilise()));
    }

    @Override
    protected String getOperatorName() {
        return "(int)";
    }

}
