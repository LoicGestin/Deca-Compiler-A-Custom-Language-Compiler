package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.codegen.codeGen;
import fr.ensimag.deca.context.*;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.DAddr;
import org.apache.commons.lang.Validate;

import java.io.PrintStream;

/**
 * @author gl29
 * @date 01/01/2024
 */
public class DeclVar extends AbstractDeclVar {


    final private AbstractIdentifier type;
    final private AbstractIdentifier varName;
    final private AbstractInitialization initialization;

    public DeclVar(AbstractIdentifier type, AbstractIdentifier varName, AbstractInitialization initialization) {
        Validate.notNull(type);
        Validate.notNull(varName);
        Validate.notNull(initialization);
        this.type = type;
        this.varName = varName;
        this.initialization = initialization;
    }

    @Override
    protected void verifyDeclVar(DecacCompiler compiler,
                                 EnvironmentExp localEnv, ClassDefinition currentClass)
            throws ContextualError {
        // Vérification du type != void
        Type t = type.verifyType(compiler);
        if (t.isVoid()) {
            throw new ContextualError("Exception : Variable type cannot be void", type.getLocation());
        }
        varName.setDefinition(new VariableDefinition(t, varName.getLocation()));
        varName.setType(t);
        initialization.verifyInitialization(compiler, t, localEnv, currentClass);
        try {
            localEnv.declare(varName.getName(), varName.getExpDefinition());
        } catch (EnvironmentExp.DoubleDefException e) {
            throw new ContextualError("Exception : Variable " + varName.getName() + " already declared", varName.getLocation());
        }

        // TODO : To finish
    }

    @Override
    public void codeGenDeclVar(DecacCompiler compiler) {
        if(codeGen.isGPRegisterRestant()){
            varName.getExpDefinition().setGPRegister(codeGen.getGPRegisterVariable());
        }
        else{
            varName.getExpDefinition().setOperand(codeGen.getRegistreVariable());
        }
        if (initialization instanceof Initialization) {
            initialization.codeGenInit(compiler, varName.getExpDefinition());
        }
    }


    @Override
    public void decompile(IndentPrintStream s) {
        if (DecacCompiler.getColor()) s.print("\033[0;31m");
        type.decompile(s);
        if (DecacCompiler.getColor()) s.print("\033[0m");
        s.print(" ");
        varName.decompile(s);
        if (!(initialization instanceof NoInitialization)) {
            s.print(" = ");
            initialization.decompile(s);
        }
        if (DecacCompiler.getColor()) {
            s.println(";", "orange");
        } else {
            s.println(";");
        }
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        type.iter(f);
        varName.iter(f);
        initialization.iter(f);
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        type.prettyPrint(s, prefix, false);
        varName.prettyPrint(s, prefix, false);
        initialization.prettyPrint(s, prefix, true);
    }
}
