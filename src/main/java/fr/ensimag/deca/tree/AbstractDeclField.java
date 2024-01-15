package fr.ensimag.deca.tree;


import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ContextualError;

public abstract class AbstractDeclField extends Tree {

    protected abstract void verifyField(DecacCompiler compiler)
            throws ContextualError;

    protected abstract void verifyFieldBody(DecacCompiler compiler) throws ContextualError;
}
