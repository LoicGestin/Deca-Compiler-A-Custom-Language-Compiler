package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.codegen.codeGen;
import fr.ensimag.deca.context.*;
import fr.ensimag.deca.tools.DecacInternalError;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.deca.tools.SymbolTable;
import fr.ensimag.ima.pseudocode.*;
import fr.ensimag.ima.pseudocode.instructions.*;
import org.apache.log4j.Logger;

import java.io.PrintStream;

public class GetAttribut extends AbstractIdentifier {
    private static final Logger LOG = Logger.getLogger(Main.class);
    private final AbstractExpr expr;
    private final AbstractIdentifier attribut;
    private Definition definition;

    public GetAttribut(AbstractExpr expr, AbstractIdentifier attribut) {
        this.expr = expr;
        this.attribut = attribut;
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv, ClassDefinition currentClass) throws ContextualError {
        Type t = expr.verifyExpr(compiler, localEnv, currentClass);
        if (!t.isClass()) {
            throw new ContextualError("Exception : L'expression n'est pas de type classe", this.getLocation());
        }

        ClassDefinition c;
        if (expr instanceof This) {
            c = currentClass;
        } else {
            c = compiler.environmentType.defOfClass(t.getName());
        }
        FieldDefinition e = (FieldDefinition) c.getMembers().get(attribut.getName());

        if(e == null) {
            throw new ContextualError("Exception : L'attribut n'existe pas dans la classe", this.getLocation());
        }

        if (!e.isField()) {
            throw new ContextualError("Exception : L'attribut n'est pas un champ de la classe", this.getLocation());
        }

        // Si c'est protected impossible d'y accéder depuis une autre classe qui n'est pas une sous-classe ou le main
        if (e.getVisibility() == Visibility.PROTECTED && !(expr instanceof This)) {
            throw new ContextualError("Exception : L'attribut est protected, impossible d'y accéder depuis une autre classe", this.getLocation());
        }
        attribut.setDefinition(e);
        setType(e.getType());

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
        codeGen.setRegistreCourant(this.getAddr(compiler), compiler);
    }

    @Override
    protected void codeGenPrint(DecacCompiler compiler) {
        if (getType().isBoolean()) {
            compiler.addInstruction(new LOAD(this.getAddr(compiler), codeGen.getRegistreLibre()));
            print_boolean(compiler);
        } else {
            compiler.addInstruction(new LOAD(this.getAddr(compiler), codeGen.getCurrentRegistreLibre()));
            compiler.addInstruction(new LOAD(codeGen.getCurrentRegistreLibre(), GPRegister.getR(1)));
            if (getType().isInt()) {
                compiler.addInstruction(new WINT());
            } else if (getType().isFloat()) {
                compiler.addInstruction(super.isHexa() ? new WFLOATX() : new WFLOAT());
            } else {
                throw new UnsupportedOperationException("Not supported yet.");
            }
        }
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
    public DAddr getAddr(DecacCompiler compiler) {
        codeGen.setAssignation(true);
        expr.codeGenInst(compiler);

        compiler.addInstruction(new CMP(new NullOperand(), codeGen.getCurrentRegistreUtilise()));
        compiler.addInstruction(new BEQ(new Label("dereferencement.null")));

        return new RegisterOffset(this.attribut.getFieldDefinition().getIndex(), codeGen.getRegistreUtilise());

    }

    @Override
    public GPRegister getGPRegister() {
        throw new UnsupportedOperationException("not yet implemented");
    }

    @Override
    public boolean isAddr(DecacCompiler compiler) {
        return false;
    }

    @Override
    public boolean isField(DecacCompiler compiler) {
        return true;
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