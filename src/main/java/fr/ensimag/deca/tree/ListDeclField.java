package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.tools.IndentPrintStream;

public class ListDeclField extends TreeList<AbstractDeclField> {
    @Override
    public void decompile(IndentPrintStream s) {
        for (AbstractDeclField f : getList()) {
            f.decompile(s);
        }
    }

    public void verifyListDeclField(DecacCompiler compiler) throws ContextualError {
        for (AbstractDeclField f : getList()) {
            f.verifyField(compiler);
        }
    }

    public void verifyListDeclFieldBody(DecacCompiler compiler) throws ContextualError {
        for (AbstractDeclField f : getList()) {
            f.verifyFieldBody(compiler);
        }
    }
}
