package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.tools.IndentPrintStream;

public class ListDeclMethod extends TreeList<AbstractDeclMethod> {
    @Override
    public void decompile(IndentPrintStream s) {
        for (AbstractDeclMethod m : getList()) {
            m.decompile(s);
        }
    }

    public void verifyListDeclMethod(DecacCompiler compiler, ClassDefinition currentClass) throws ContextualError {
        for (AbstractDeclMethod m : getList()) {
            m.verifyMethod(compiler, currentClass);
        }
    }

    public void verifyListDeclMethodBody(DecacCompiler compiler) throws ContextualError {
        for (AbstractDeclMethod m : getList()) {
            m.verifyMethodBody(compiler);
        }
    }
}
