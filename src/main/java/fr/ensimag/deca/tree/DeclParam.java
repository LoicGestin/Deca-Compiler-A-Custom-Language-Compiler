package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.*;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.RegisterOffset;

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
    protected Type verifyParam(DecacCompiler compiler, EnvironmentExp localEnv, ClassDefinition currentClass) throws ContextualError {
        Type t = type.verifyType(compiler);
        if (t.isVoid()) {
            throw new ContextualError("Exception : Variable type cannot be void", type.getLocation());
        }
        name.setDefinition(new ParamDefinition(t, name.getLocation()));

        try {
            localEnv.declare(name.getName(), name.getExpDefinition());
        } catch (EnvironmentExp.DoubleDefException e) {
            throw new ContextualError("Exception : Variable " + name.getName() + " already declared", name.getLocation());
        }

        name.setType(t);
        return getType();
    }

    @Override
    public void codeGenParam(DecacCompiler compiler, ClassDefinition currentClass, EnvironmentExp envParam, int i) {
        name.getExpDefinition().setOperand(new RegisterOffset(-3 - i, Register.LB));
    }

}
