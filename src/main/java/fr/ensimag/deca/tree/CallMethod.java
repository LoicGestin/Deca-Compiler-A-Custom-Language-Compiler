package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.codegen.codeGen;
import fr.ensimag.deca.context.*;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.*;
import fr.ensimag.ima.pseudocode.instructions.*;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.NullOperand;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.RegisterOffset;
import fr.ensimag.ima.pseudocode.instructions.*;


import java.io.PrintStream;
import java.util.List;


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
        compiler.addInstruction(new ADDSP(1 + arguments.size()));
        codeGen.setAssignation(true);
        expr.codeGenInst(compiler);
        compiler.addInstruction(new STORE(codeGen.getRegistreUtilise(), new RegisterOffset(0, Register.SP)));
        List<AbstractExpr> exprList = arguments.getList();
        for (int i = 0; i < exprList.size() ; i++) {
            exprList.get(i).codeGenInst(compiler);
            compiler.addInstruction(new STORE(codeGen.getRegistreUtilise(), new RegisterOffset(-i -1, Register.SP)));
        }
        compiler.addInstruction(new LOAD(new RegisterOffset(0, Register.SP), codeGen.getCurrentRegistreLibre()));
        compiler.addInstruction(new CMP(new NullOperand(), codeGen.getCurrentRegistreLibre()));
        compiler.addInstruction(new BEQ(new Label("dereferencement.null")));
        compiler.addInstruction(new LOAD(new RegisterOffset(0, codeGen.getCurrentRegistreLibre()), codeGen.getCurrentRegistreLibre()));
        compiler.addInstruction(new BSR(new RegisterOffset(method.getMethodDefinition().getIndex() , codeGen.getCurrentRegistreLibre())));
        compiler.addInstruction(new SUBSP(1 + arguments.size()));
        compiler.addComment("new line -----------------------------------");
    }

    @Override
    protected void codeGenPrint(DecacCompiler compiler) {
        this.codeGenInst(compiler);
        if(this.method.getMethodDefinition().getType().isBoolean()){
            print_boolean(compiler);
        }
        compiler.addInstruction(new LOAD(Register.getR(0),Register.getR(1)));
        if(this.method.getMethodDefinition().getType().isInt()){
            compiler.addInstruction(new WINT());
        }
        else if(this.method.getMethodDefinition().getType().isFloat()){
            compiler.addInstruction(new WFLOAT());
        }
    }
}