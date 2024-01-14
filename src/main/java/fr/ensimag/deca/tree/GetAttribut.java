package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.*;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.deca.tools.SymbolTable;
import fr.ensimag.ima.pseudocode.DAddr;
import fr.ensimag.ima.pseudocode.GPRegister;

import java.io.PrintStream;

public class GetAttribut extends AbstractIdentifier {
    private final AbstractExpr expr;
    private final AbstractIdentifier attribut;

    public GetAttribut(AbstractExpr expr, AbstractIdentifier attribut) {
        this.expr = expr;
        this.attribut = attribut;
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv, ClassDefinition currentClass) throws ContextualError {
        throw new UnsupportedOperationException("not yet implemented");
    }

    @Override
    public void decompile(IndentPrintStream s) {
        expr.decompile(s);
        s.print(".");
        attribut.decompile(s);
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        expr.prettyPrint(s, prefix, false);
        attribut.prettyPrint(s, prefix, true);
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        expr.iterChildren(f);
        attribut.iterChildren(f);
    }

    @Override
    protected void codeGenInst(DecacCompiler compiler) {
        throw new UnsupportedOperationException("not yet implemented");
    }

    @Override
    public ClassDefinition getClassDefinition() {
        throw new UnsupportedOperationException("not yet implemented");
    }

    @Override
    public Definition getDefinition() {
        throw new UnsupportedOperationException("not yet implemented");
    }

    @Override
    public void setDefinition(Definition definition) {
        throw new UnsupportedOperationException("not yet implemented");
    }

    @Override
    public FieldDefinition getFieldDefinition() {
        throw new UnsupportedOperationException("not yet implemented");
    }

    @Override
    public MethodDefinition getMethodDefinition() {
        throw new UnsupportedOperationException("not yet implemented");
    }

    @Override
    public SymbolTable.Symbol getName() {
        throw new UnsupportedOperationException("not yet implemented");
    }

    @Override
    public DAddr getAddr() {
        throw new UnsupportedOperationException("not yet implemented");
    }

    @Override
    public GPRegister getGPRegister() {
        throw new UnsupportedOperationException("not yet implemented");
    }

    @Override
    public boolean isAddr() {
        throw new UnsupportedOperationException("not yet implemented");
    }

    @Override
    public ExpDefinition getExpDefinition() {
        throw new UnsupportedOperationException("not yet implemented");
    }

    @Override
    public VariableDefinition getVariableDefinition() {
        throw new UnsupportedOperationException("not yet implemented");
    }

    @Override
    public Type verifyType(DecacCompiler compiler) throws ContextualError {
        throw new UnsupportedOperationException("not yet implemented");
    }
}