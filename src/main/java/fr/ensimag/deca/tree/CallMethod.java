package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.tools.IndentPrintStream;

import java.io.PrintStream;


public class CallMethod extends AbstractExpr {


    private final AbstractExpr expr;
    private final AbstractIdentifier method;
    private final ListExpr arguments;

    public CallMethod(AbstractExpr expr, AbstractIdentifier method, ListExpr arguments) {
        this.expr = expr;
        this.method = method;
        this.arguments = arguments;
    }

    /**
     * Verify the expression for contextual error.
     * <p>
     * implements non-terminals "expr" and "lvalue"
     * of [SyntaxeContextuelle] in pass 3
     *
     * @param compiler     (contains the "env_types" attribute)
     * @param localEnv     Environment in which the expression should be checked
     *                     (corresponds to the "env_exp" attribute)
     * @param currentClass Definition of the class containing the expression
     *                     (corresponds to the "class" attribute)
     *                     is null in the main bloc.
     * @return the Type of the expression
     * (corresponds to the "type" attribute)
     */
    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv, ClassDefinition currentClass) throws ContextualError {
        return null;
    }

    /**
     * Implements non-terminal "inst" of [SyntaxeContextuelle] in pass 3
     *
     * @param compiler     contains the "env_types" attribute
     * @param localEnv     corresponds to the "env_exp" attribute
     * @param currentClass corresponds to the "class" attribute (null in the main bloc).
     * @param returnType   corresponds to the "return" attribute (void in the main bloc).
     */

    /**
     * Display the tree as a (compilable) source program
     *
     * @param s Buffer to which the result will be written.
     */
    @Override
    public void decompile(IndentPrintStream s) {
        expr.decompile(s);
        s.print(".");
        method.decompile(s);
        s.print("(");
        arguments.decompile(s);
        s.print(")");
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
        throw new UnsupportedOperationException("not yet implemented");
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
        method.iterChildren(f);
        arguments.iterChildren(f);
    }
}