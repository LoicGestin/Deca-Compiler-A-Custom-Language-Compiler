package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.*;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.deca.tools.SymbolTable;
import fr.ensimag.ima.pseudocode.DAddr;
import fr.ensimag.ima.pseudocode.GPRegister;

import java.io.PrintStream;

public class This extends AbstractIdentifier {

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv, ClassDefinition currentClass) throws ContextualError {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void decompile(IndentPrintStream s) {
        if (DecacCompiler.getColor()) s.print("\033[0;31m");
        s.print("this");
        if (DecacCompiler.getColor()) s.print("\033[0m");
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        s.print("this");
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        // Leaf node => nothing to do.
    }

    @Override
    public ClassDefinition getClassDefinition() {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public Definition getDefinition() {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void setDefinition(Definition definition) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public FieldDefinition getFieldDefinition() {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public MethodDefinition getMethodDefinition() {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public SymbolTable.Symbol getName() {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public DAddr getAddr() {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public GPRegister getGPRegister() {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public boolean isAddr() {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public ExpDefinition getExpDefinition() {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public VariableDefinition getVariableDefinition() {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public Type verifyType(DecacCompiler compiler) throws ContextualError {
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
