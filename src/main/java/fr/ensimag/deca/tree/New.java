package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.codegen.codeGen;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.RegisterOffset;
import fr.ensimag.ima.pseudocode.instructions.*;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.RegisterOffset;
import fr.ensimag.ima.pseudocode.instructions.*;
import org.apache.commons.lang.Validate;

import java.io.PrintStream;

public class New extends AbstractExpr {
    private final AbstractIdentifier type;

    public New(AbstractIdentifier type) {
        Validate.notNull(type);
        this.type = type;
    }

    @Override
    public void decompile(IndentPrintStream s) {
        if (DecacCompiler.getColor()) {
            s.print("new ", "purple");
        } else {
            s.print("new ");
        }
        type.decompile(s);
        s.print("()");
    }

    /**
     * Used internally by {@link #prettyPrint}. Must call prettyPrint() on each
     * children.
     *
     * @param s
     * @param prefix
     */
    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        type.prettyPrint(s, prefix, true);
    }

    /**
     * Function used internally by {@link #iter(TreeFunction)}. Must call iter() on each
     * child of the tree.
     *
     * @param f
     */
    @Override
    protected void iterChildren(TreeFunction f) {
        type.iter(f);
    }


    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv, ClassDefinition currentClass) throws ContextualError {
        Type t = type.verifyType(compiler);
        if (!t.isClass()) {
            throw new ContextualError("new must be used with a class", getLocation());
        }
        setType(t);
        return getType();
    }
    @Override
    protected void codeGenInst(DecacCompiler compiler) {
        compiler.addInstruction(new NEW(type.getClassDefinition().getNumberOfFields() + 1, codeGen.getRegistreLibre()));
        compiler.addInstruction(new BOV(new Label("pile_pleine")));
        compiler.addInstruction(new LEA(new RegisterOffset(type.getClassDefinition().getAdressTable(), Register.GB), Register.R0));
        compiler.addInstruction(new STORE(Register.R0, new RegisterOffset(0, codeGen.getCurrentRegistreUtilise())));
        compiler.addInstruction(new PUSH(codeGen.getCurrentRegistreUtilise()));
        compiler.addInstruction(new BSR(compiler.classLabel.addLabel("init." + type.getName().getName())));
        compiler.addInstruction(new POP(codeGen.getCurrentRegistreUtilise()));
    }
}
