package fr.ensimag.deca.tree;


import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.Type;

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
        this.setType(compiler.environmentType.BOOLEAN);
        return this.getType();
    }
}
