package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.Signature;
import fr.ensimag.deca.context.Type;
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

    public Signature verifyListDeclParamMembers(DecacCompiler compiler, ClassDefinition currentClass) throws ContextualError {
        Signature sig = new Signature();
        for (AbstractDeclParam p : getList()) {
            Type t = p.verifyParam(compiler, currentClass);
            sig.add(t);
        }
        return sig;
    }
}
