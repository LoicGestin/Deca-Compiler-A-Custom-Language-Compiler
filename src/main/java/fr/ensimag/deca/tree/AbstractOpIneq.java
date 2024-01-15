package fr.ensimag.deca.tree;


import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.*;

/**
 * @author gl29
 * @date 01/01/2024
 */
public abstract class AbstractOpIneq extends AbstractOpCmp {

    public AbstractOpIneq(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv, ClassDefinition currentClass) throws ContextualError {
        super.verifyExpr(compiler, localEnv, currentClass);
        Type t1 = this.getLeftOperand().getType();
        Type t2 = this.getRightOperand().getType();

        if (t1.isInt() || t1.isFloat()) {
            this.setType(new BooleanType(compiler.createSymbol("boolean")));
            return this.getType();
        } else {
            throw new ContextualError("Exception : type incompatible : " + t1 + " and " + t2, this.getLocation());
        }

    }
}
