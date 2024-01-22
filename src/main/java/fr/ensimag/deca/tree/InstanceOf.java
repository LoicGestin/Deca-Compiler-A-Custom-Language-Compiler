package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.Type;

public class InstanceOf extends AbstractOpExactCmp {
    public InstanceOf(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv, ClassDefinition currentClass) throws ContextualError {
        Type t1 = this.getLeftOperand().verifyExpr(compiler, localEnv, currentClass);
        Type t2 = this.getRightOperand().verifyExpr(compiler, localEnv, currentClass);
        if (!(t1.isClassOrNull() && t2.isClass())) {
            throw new ContextualError("Exception : type incompatible : " + t1 + " and " + t2, getLeftOperand().getLocation());
        }

        setType(compiler.environmentType.BOOLEAN);
        return getType();
    }

    @Override
    public void codeGenOp(DecacCompiler compiler) {
        // a InstanceOf B
        // On parcours la chaine de pointeur



    }

    @Override
    protected String getOperatorName() {
        return "instanceof";
    }
}
