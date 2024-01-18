package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.*;
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
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv, ClassDefinition currentClass)
            throws ContextualError {

        LOG.debug("verify CallMethod: start");

        Type t = expr.verifyExpr(compiler, localEnv, currentClass);
        if (!t.isClass()) {
            throw new ContextualError("L'expression n'est pas de type classe", this.getLocation());
        }


        ClassDefinition c;
        if (expr instanceof This) {
            c = currentClass;
        } else {
            c = compiler.environmentType.defOfClass(t.getName());
        }

        Definition def = c.getMembers().get(method.getName());

        if (!def.isMethod()) {
            throw new ContextualError("L'identificateur n'est pas une méthode", this.getLocation());
        }

        MethodDefinition method = (MethodDefinition) def;


        Signature sig = method.getSignature();

        if (sig.size() != arguments.size()) {
            throw new ContextualError("Exception : Wrong number of arguments in method call : " + sig.size() + " expected, " + arguments.size() + " given", getLocation());
        }

        for (int n = 0; n < sig.size(); n++) {
            AbstractExpr e = arguments.getList().get(n);
            e.verifyRValue(compiler, localEnv, currentClass, sig.paramNumber(n));
        }

        setType(method.getType());

        LOG.debug("verify CallMethod: end");

        return method.getType();
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
        expr.prettyPrint(s, prefix, false);
        method.prettyPrint(s, prefix, false);
        arguments.prettyPrint(s, prefix, false);
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