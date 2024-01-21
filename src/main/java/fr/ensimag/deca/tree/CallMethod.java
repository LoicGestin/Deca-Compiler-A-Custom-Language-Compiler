package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.codegen.codeGen;
import fr.ensimag.deca.context.*;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.NullOperand;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.RegisterOffset;
import fr.ensimag.ima.pseudocode.instructions.*;


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

        MethodDefinition methodDefinition = (MethodDefinition) def;
        method.setDefinition(methodDefinition);

        Signature sig = methodDefinition.getSignature();

        if (sig.size() != arguments.size()) {
            throw new ContextualError("Exception : Wrong number of arguments in method call : " + sig.size() + " expected, " + arguments.size() + " given", getLocation());
        }

        for (int n = 0; n < sig.size(); n++) {
            AbstractExpr e = arguments.getList().get(n);
            e.verifyRValue(compiler, localEnv, currentClass, sig.paramNumber(n));
        }

        setType(methodDefinition.getType());

        LOG.debug("verify CallMethod: end");

        return methodDefinition.getType();
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

    @Override
    protected void codeGenInst(DecacCompiler compiler) {
        // On reserve de la place pour les parametres
        compiler.addInstruction(new ADDSP(arguments.size() + 1));

        // On empile le parametre implicite (this) dans SP
        codeGen.setAssignation(true);
        expr.codeGenInst(compiler);
        compiler.addInstruction(new STORE(codeGen.getRegistreUtilise(), new RegisterOffset(0, Register.SP)));

        // On empile les parametres dans SP dans une boucle
        for (int i = 0; i < arguments.size(); i++) {
            codeGen.setAssignation(true);
            arguments.getList().get(i).codeGenInst(compiler);
            compiler.addInstruction(new STORE(codeGen.getRegistreUtilise(), new RegisterOffset(-i - 1, Register.SP)));
        }

        // On récupère le parametre implicite (this) dans SP
        compiler.addInstruction(new LOAD(new RegisterOffset(0, Register.SP), codeGen.getRegistreLibre()));

        // On teste si le parametre implicite est null
        compiler.addInstruction(new CMP(new NullOperand(), codeGen.getCurrentRegistreUtilise()));
        compiler.addInstruction(new BEQ(new Label("dereferencement.null")));

        // On récupère l'adresse de la méthode dans le VMT
        compiler.addInstruction(new LOAD(new RegisterOffset(0, codeGen.getCurrentRegistreUtilise()), codeGen.getCurrentRegistreUtilise()));

        // On appelle la méthode
        compiler.addInstruction(new BSR(new RegisterOffset(method.getMethodDefinition().getIndex(), codeGen.getCurrentRegistreUtilise())));

        // On dépile les parametres
        compiler.addInstruction(new SUBSP(arguments.size() + 1));
    }

}