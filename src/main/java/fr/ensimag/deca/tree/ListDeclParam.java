package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.tools.IndentPrintStream;

public class ListDeclParam extends TreeList<AbstractDeclParam> {
    @Override
    public void decompile(IndentPrintStream s) {
        boolean first = true;
        for (AbstractDeclParam p : getList()) {
            if (!first) {
                s.print(", ");
            }
            p.decompile(s);
            first = false;
        }

    }

    public void verifyListDeclParamMembers(DecacCompiler compiler) throws ContextualError {
        for (AbstractDeclParam p : getList()) {
            p.verifyParamMembers(compiler);
        }
    }
}
