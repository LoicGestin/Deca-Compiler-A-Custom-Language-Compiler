package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.codegen.codeGen;
import fr.ensimag.deca.context.*;
import fr.ensimag.deca.tools.IndentPrintStream;
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


        try {
            localEnv.declare(varName.getName(), varName.getExpDefinition());
        } catch (EnvironmentExp.DoubleDefException e) {
            throw new ContextualError("Exception : Variable already declared", varName.getLocation());
        }
        initialization.verifyInitialization(compiler, t, localEnv, currentClass);

        codeGen.addtableDesDeclaration(codeGen.getMethod(), varName.getName().toString());
    }


    @Override
    public void codeGenDeclVar(DecacCompiler compiler) {
        boolean isGPRegisterRemaining = codeGen.isGPRegisterRestant(varName.getName().toString());

        ExpDefinition expDefinition = varName.getExpDefinition();

        if (isGPRegisterRemaining) {
            expDefinition.setGPRegister(codeGen.getGPRegisterVariable());
        } else {
            expDefinition.setOperand(codeGen.getRegistreVariable());
        }

        /*
        if (DecacCompiler.getDebug()) {
            if (initialization instanceof Initialization) {
                compiler.addComment("Declaration de la variable " + varName.getName() + " et initialisation" + (expDefinition.isAddr() ? " par adresse : " + expDefinition.getOperand() : " par registre : " + expDefinition.getGPRegister()));
            }
            if (initialization instanceof NoInitialization) {
                compiler.addComment("Declaration de la variable " + varName.getName() + (expDefinition.isAddr() ? " par adresse : " + expDefinition.getOperand() : " par registre : " + expDefinition.getGPRegister()));
            }
        }*/
        initialization.codeGenInit(compiler, expDefinition);


    }


    @Override
    public void decompile(IndentPrintStream s) {
        DeclField.print_def_variable(s, type, varName, initialization);
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
