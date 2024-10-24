package fr.ensimag.deca.tree;


import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;

public abstract class AbstractDeclField extends Tree {

    protected abstract void verifyField(DecacCompiler compiler, ClassDefinition currentClass)
            throws ContextualError;

    protected abstract void verifyFieldBody(DecacCompiler compiler, ClassDefinition currentClass) throws ContextualError;

    public abstract void codeGenFieldPasseTwo(DecacCompiler compiler, ClassDefinition classDefinition);

}
