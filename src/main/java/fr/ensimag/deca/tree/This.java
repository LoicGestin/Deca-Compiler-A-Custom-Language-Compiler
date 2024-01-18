package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.*;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.deca.tools.SymbolTable;
import fr.ensimag.ima.pseudocode.DAddr;
import fr.ensimag.ima.pseudocode.GPRegister;

import java.io.PrintStream;

public class This extends AbstractExpr {

    private final boolean value;

    public This(boolean value) {
        this.value = value;
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv, ClassDefinition currentClass) throws ContextualError {
        this.setType(currentClass.getType());
        return getType();
    }

    @Override
    public void decompile(IndentPrintStream s) {
        if (DecacCompiler.getColor()) s.print("\033[0;31m");
        s.print("this");
        if (DecacCompiler.getColor()) s.print("\033[0m");
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        // Leaf node => nothing to do.
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        // Leaf node => nothing to do.
    }
}
