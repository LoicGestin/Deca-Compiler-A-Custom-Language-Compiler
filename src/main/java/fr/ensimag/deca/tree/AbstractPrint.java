package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.codegen.codeGen;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.tools.IndentPrintStream;
import org.apache.commons.lang.Validate;

import java.io.PrintStream;

/**
 * Print statement (print, println, ...).
 *
 * @author gl29
 * @date 01/01/2024
 */
public abstract class AbstractPrint extends AbstractInst {
    private final boolean printHex;
    private final ListExpr arguments;

    public AbstractPrint(boolean printHex, ListExpr arguments) {
        Validate.notNull(arguments);
        this.arguments = arguments;
        this.printHex = printHex;
    }

    abstract String getSuffix();

    public ListExpr getArguments() {
        return arguments;
    }

    @Override
    protected void verifyInst(DecacCompiler compiler, EnvironmentExp localEnv,
                              ClassDefinition currentClass, Type returnType)
            throws ContextualError {

        // Check that each argument is compatible with print type
        for (AbstractExpr a : getArguments().getList()) {
            Type t = a.verifyExpr(compiler, localEnv, currentClass);
            // MAYBE CHANGE WHEN OBJECT WILL BE IMPLEMENTED
            //if (!t.isInt() && !t.isFloat() && !t.isString() && !t.isBoolean()) {
            //    throw new ContextualError("Exception : Argument of print must be int, float, boolean or string", a.getLocation());
            //}
        }
    }

    @Override
    protected void codeGenInst(DecacCompiler compiler) {
        for (AbstractExpr a : getArguments().getList()) {
            a.setHexa(getPrintHex());
            codeGen.setAssignation(true);
            a.codeGenPrint(compiler);
        }
    }

    private boolean getPrintHex() {
        return printHex;
    }

    @Override
    public void decompile(IndentPrintStream s) {
        if (DecacCompiler.getColor()) {
            s.print("print" + getSuffix() + ((getPrintHex()) ? "x" : ""), "purple");
            s.print("(" + arguments.decompile() + ")");
            s.print(";", "orange");
        } else {
            s.print("print" + getSuffix() + ((getPrintHex()) ? "x" : "") + "(" + arguments.decompile() + ");");
        }
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        arguments.iter(f);
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        arguments.prettyPrint(s, prefix, true);
    }

}
