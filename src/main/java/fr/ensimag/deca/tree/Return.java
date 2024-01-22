package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.codegen.codeGen;
import fr.ensimag.deca.context.*;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.instructions.BRA;
import fr.ensimag.ima.pseudocode.instructions.LOAD;

import java.io.PrintStream;

public class Return extends AbstractInst {

    private AbstractExpr expr;
    private String method;

    public Return(AbstractExpr expr) {
        this.expr = expr;
    }

    /**
     * Implements non-terminal "inst" of [SyntaxeContextuelle] in pass 3
     *
     * @param compiler     contains the "env_types" attribute
     * @param localEnv     corresponds to the "env_exp" attribute
     * @param currentClass corresponds to the "class" attribute (null in the main bloc).
     * @param returnType   corresponds to the "return" attribute (void in the main bloc).
     */
    @Override
    protected void verifyInst(DecacCompiler compiler, EnvironmentExp localEnv, ClassDefinition currentClass, Type returnType) throws ContextualError {
        Type t = expr.verifyExpr(compiler, localEnv, currentClass);
        if (t.isInt() && returnType.isFloat()) {
            ConvFloat conv = new ConvFloat(expr);
            conv.verifyExpr(compiler, localEnv, currentClass);
            this.expr = conv;
            t = expr.getType();
        } else if (t.isClass() && returnType.isClass()) {
            ClassType tClass = t.asClassType("Exception : Return type is not a class", this.getLocation());
            ClassType returnTypeClass = returnType.asClassType("Return type is not a class", this.getLocation());
            if (tClass != returnTypeClass) {
                if (!tClass.isSubClassOf(returnTypeClass)) {
                    throw new ContextualError("Exception : Return type is not a subclass of the method type", this.getLocation());
                }
            }
            t = returnTypeClass;

        }
        if (!t.sameType(returnType)) {
            throw new ContextualError("Exception : Return type is not the same as the method type", this.getLocation());
        }

        expr.setType(returnType);
    }

    /**
     * Generate assembly code for the instruction.
     *
     * @param compiler
     */
    @Override
    protected void codeGenInst(DecacCompiler compiler) {
        expr.codeGenInst(compiler);
        // On met le résultat dans le registre R0
        compiler.addInstruction(new LOAD(codeGen.getRegistreUtilise(), Register.R0));

        // Get the name of the method
        compiler.addInstruction(new BRA(new Label("fin." + codeGen.getCurrentMethod())));
    }

    /**
     * Display the tree as a (compilable) source program
     *
     * @param s Buffer to which the result will be written.
     */
    @Override
    public void decompile(IndentPrintStream s) {
        if (DecacCompiler.getColor()) {
            s.print("return ", "purple");
        } else {
            s.print("return ");
        }
        expr.decompile(s);
        if (DecacCompiler.getColor()) {
            s.println(";", "purple");
        } else {
            s.println(";");
        }
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
        expr.prettyPrint(s, prefix, true);
    }

    @Override
    protected void prettyPrintType(PrintStream s, String prefix) {
        Type t = expr.getType();
        if (t != null) {
            s.print(prefix);
            s.print("type: ");
            s.print(t);
            s.println();
        }
    }


    /**
     * Function used internally by {@link #iter(TreeFunction)}. Must call iter() on each
     * child of the tree.
     *
     * @param f
     */
    @Override
    protected void iterChildren(TreeFunction f) {
        expr.iterChildren(f);
    }
}
