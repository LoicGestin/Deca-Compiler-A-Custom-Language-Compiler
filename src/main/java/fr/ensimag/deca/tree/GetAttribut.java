package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.*;
import fr.ensimag.deca.tools.DecacInternalError;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.deca.tools.SymbolTable;
import fr.ensimag.ima.pseudocode.DAddr;
import fr.ensimag.ima.pseudocode.GPRegister;

import java.io.PrintStream;

public class GetAttribut extends AbstractIdentifier {
    private final AbstractExpr expr;
    private final AbstractIdentifier attribut;

    private Definition definition;

    public GetAttribut(AbstractExpr expr, AbstractIdentifier attribut) {
        this.expr = expr;
        this.attribut = attribut;
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv, ClassDefinition currentClass) throws ContextualError {
        expr.verifyExpr(compiler, localEnv, currentClass);
        Type tAttribut = attribut.verifyExpr(compiler, localEnv, currentClass);

        if(attribut.getDefinition().isClass()){
            throw new ContextualError("GetAttribut on class type", attribut.getLocation());
        }

        // si protected
        FieldDefinition attributDef = attribut.getFieldDefinition();
        if (attributDef.getVisibility() == Visibility.PROTECTED && (currentClass == null ||
                !currentClass.getType().isSubClassOf(currentClass.getSuperClass().getType()))) {
            throw new ContextualError("The field is protected",attribut.getLocation());
        }

        setType(tAttribut);
        return getType();

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
        try {
            return (ClassDefinition) definition;
        } catch (ClassCastException e) {
            throw new DecacInternalError(
                    "Identifier "
                            + getName()
                            + " is not a class identifier, you can't call getClassDefinition on it");
        }
    }

    @Override
    public Definition getDefinition() {
        return definition;
    }

    @Override
    public void setDefinition(Definition definition) {
        this.definition = definition;
    }

    @Override
    public FieldDefinition getFieldDefinition() {
        try {
            return (FieldDefinition) definition;
        } catch (ClassCastException e) {
            throw new DecacInternalError(
                    "Identifier "
                            + getName()
                            + " is not a field identifier, you can't call getFieldDefinition on it");
        }
    }

    @Override
    public MethodDefinition getMethodDefinition() {
        try {
            return (MethodDefinition) definition;
        } catch (ClassCastException e) {
            throw new DecacInternalError(
                    "Identifier "
                            + getName()
                            + " is not a method identifier, you can't call getMethodDefinition on it");
        }
    }

    @Override
    public SymbolTable.Symbol getName() {
        return attribut.getName();
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
        try {
            return (ExpDefinition) definition;
        } catch (ClassCastException e) {
            throw new DecacInternalError(
                    "Identifier "
                            + getName()
                            + " is not a Exp identifier, you can't call getExpDefinition on it");
        }
    }

    @Override
    public VariableDefinition getVariableDefinition() {
        try {
            return (VariableDefinition) definition;
        } catch (ClassCastException e) {
            throw new DecacInternalError(
                    "Identifier "
                            + getName()
                            + " is not a variable identifier, you can't call getVariableDefinition on it");
        }
    }

    @Override
    public Type verifyType(DecacCompiler compiler) throws ContextualError {
        throw new UnsupportedOperationException("not yet implemented");
    }
}