package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ContextualError;

public abstract class AbstractDeclParam extends Tree {

    protected abstract void verifyParam(DecacCompiler compiler)
            throws ContextualError;

    protected abstract void verifyParamMembers(DecacCompiler compiler)
            throws ContextualError;

    protected abstract void verifyParamBody(DecacCompiler compiler)
            throws ContextualError;
}
