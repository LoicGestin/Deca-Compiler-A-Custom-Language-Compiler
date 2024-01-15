package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.tools.IndentPrintStream;

import java.io.PrintStream;

public class DeclParam extends AbstractDeclParam {

    public DeclParam(AbstractIdentifier type, AbstractIdentifier name) {
        super(type, name);
    }

    @Override
    public void decompile(IndentPrintStream s) {
        if (DecacCompiler.getColor()) s.print("\033[0;31m");
        this.getTypeIdent().decompile(s);
        if (DecacCompiler.getColor()) s.print("\033[0m");
        s.print(" ");
        this.getName().decompile(s);
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        this.getTypeIdent().prettyPrint(s, prefix, true);
        this.getName().prettyPrint(s, prefix, true);
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        this.getTypeIdent().iter(f);
        this.getName().iter(f);
    }

    @Override
    protected void verifyParam(DecacCompiler compiler) throws ContextualError {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    protected void verifyParamMembers(DecacCompiler compiler) throws ContextualError {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    protected void verifyParamBody(DecacCompiler compiler) throws ContextualError {
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
