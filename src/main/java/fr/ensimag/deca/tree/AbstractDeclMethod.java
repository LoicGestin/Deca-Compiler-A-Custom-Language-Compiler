package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;

public abstract class AbstractDeclMethod extends Tree {
    protected abstract void verifyMethod(DecacCompiler compiler, ClassDefinition currentClass)
            throws ContextualError;

    protected abstract void verifyMethodBody(DecacCompiler compiler, ClassDefinition currentClass)
            throws ContextualError;

}
