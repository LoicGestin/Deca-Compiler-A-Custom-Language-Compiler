package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.tools.IndentPrintStream;

public class ListDeclMethod extends TreeList<DeclMethod> {
    @Override
    public void decompile(IndentPrintStream s) {
        for (DeclMethod m : getList()) {
            m.decompile(s);
        }
    }

    public void verifyListDeclMethod(DecacCompiler compiler, ClassDefinition currentClass) throws ContextualError {
        for (DeclMethod m : getList()) {
            m.verifyMethod(compiler, currentClass);
        }
    }

    public void verifyListDeclMethodBody(DecacCompiler compiler, ClassDefinition currentClass) throws ContextualError {
        for (DeclMethod m : getList()) {
            m.verifyMethodBody(compiler, currentClass);
        }
    }

    public void codeGenListDeclMethod(DecacCompiler compiler, ClassDefinition currentClass) {
        for (DeclMethod m : getList()) {
            m.codeGenMethodPasseTwo(compiler, currentClass);
        }
    }
}
