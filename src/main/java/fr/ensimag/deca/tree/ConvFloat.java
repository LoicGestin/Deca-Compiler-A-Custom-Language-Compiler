package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.codegen.codeGen;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.Type;
import fr.ensimag.ima.pseudocode.instructions.FLOAT;

/**
 * Conversion of an int into a float. Used for implicit conversions.
 *
 * @author gl29
 * @date 01/01/2024
 */
public class ConvFloat extends AbstractUnaryExpr {
    public ConvFloat(AbstractExpr operand) {
        super(operand);
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv,
                           ClassDefinition currentClass) throws ContextualError {
        Type typeOp = getOperand().verifyExpr(compiler, localEnv, currentClass);
        if (typeOp.isInt()) {
            setType(compiler.environmentType.FLOAT);
            return getType();
        } else {
            throw new ContextualError("Conversion vers un flottant d'un non entier", getLocation());
        }

    }

    @Override
    protected void codeGenInst(DecacCompiler compiler) {
        getOperand().codeGenInst(compiler);
        compiler.libererRegistre();
        compiler.addInstruction(new FLOAT(codeGen.getCurrentRegistreUtilise(), codeGen.getCurrentRegistreUtilise()));
    }


    @Override
    protected String getOperatorName() {
        return "(float)";
    }

}
