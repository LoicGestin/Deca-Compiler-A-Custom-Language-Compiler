package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.codegen.codeGen;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.Type;
import fr.ensimag.ima.pseudocode.instructions.OPP;

/**
 * @author gl29
 * @date 01/01/2024
 */
public class UnaryMinus extends AbstractUnaryExpr {

    public UnaryMinus(AbstractExpr operand) {
        super(operand);
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv,
                           ClassDefinition currentClass) throws ContextualError {
        Type op = this.getOperand().verifyExpr(compiler, localEnv, currentClass);
        if (op.isInt() || op.isFloat()) {
            this.setType(op);
            return op;
        } else {
            throw new ContextualError("Exception : Unary minus can only be applied to int or float", this.getLocation());
        }
    }

    @Override
    public void codeGenOp(DecacCompiler compiler) {
        compiler.addInstruction(new OPP(codeGen.getCurrentRegistreUtilise(), codeGen.getCurrentRegistreUtilise()));
    }


    @Override
    protected String getOperatorName() {
        return "-";
    }

}
