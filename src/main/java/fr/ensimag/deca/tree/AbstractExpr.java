package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.codegen.codeGen;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.tools.DecacInternalError;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.ImmediateString;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.instructions.BEQ;
import fr.ensimag.ima.pseudocode.instructions.BRA;
import fr.ensimag.ima.pseudocode.instructions.CMP;
import fr.ensimag.ima.pseudocode.instructions.WSTR;
import org.apache.commons.lang.Validate;

import java.io.PrintStream;

/**
 * Expression, i.e. anything that has a value.
 *
 * @author gl29
 * @date 01/01/2024
 */
public abstract class AbstractExpr extends AbstractInst {
    private Type type;
    private boolean isHexa = false;

    static void print_boolean(DecacCompiler compiler) {

        compiler.addInstruction(new CMP(1, codeGen.getRegistreUtilise()));
        Label vrai = compiler.labelTable.addLabel("vrai_Identifier");
        Label fin = compiler.labelTable.addLabel("fin_Identifier");
        compiler.addInstruction(new BEQ(vrai));
        compiler.addInstruction(new WSTR(new ImmediateString("false")));
        compiler.addInstruction(new BRA(fin));
        compiler.addLabel(vrai);
        compiler.addInstruction(new WSTR(new ImmediateString("true")));
        compiler.addLabel(fin);
    }

    public boolean isHexa() {
        return isHexa;
    }

    public void setHexa(boolean hexa) {
        isHexa = hexa;
    }

    /**
     * @return true if the expression does not correspond to any concrete token
     * in the source code (and should be decompiled to the empty string).
     */
    boolean isImplicit() {
        if (this instanceof Identifier) {
            return ((Identifier) this).getName().getName().equals("this");
        }
        return false;
    }

    /**
     * Get the type decoration associated to this expression (i.e. the type computed by contextual verification).
     */
    public Type getType() {
        return type;
    }

    protected void setType(Type type) {
        Validate.notNull(type);
        this.type = type;
    }

    @Override
    protected void checkDecoration() {
        if (getType() == null) {
            throw new DecacInternalError("Expression " + decompile() + " has no Type decoration");
        }
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
    public abstract Type verifyExpr(DecacCompiler compiler,
                                    EnvironmentExp localEnv, ClassDefinition currentClass)
            throws ContextualError;

    /**
     * Verify the expression in right hand-side of (implicit) assignments
     * <p>
     * implements non-terminal "rvalue" of [SyntaxeContextuelle] in pass 3
     *
     * @param compiler     contains the "env_types" attribute
     * @param localEnv     corresponds to the "env_exp" attribute
     * @param currentClass corresponds to the "class" attribute
     * @param expectedType corresponds to the "type1" attribute
     * @return this with an additional ConvFloat if needed...
     */
    public AbstractExpr verifyRValue(DecacCompiler compiler,
                                     EnvironmentExp localEnv, ClassDefinition currentClass,
                                     Type expectedType)
            throws ContextualError {
        Type cour = verifyExpr(compiler, localEnv, currentClass);
        if (!compiler.environmentType.compatible(expectedType, cour)) {
            throw new ContextualError("Exception : Incompatible types in assignment", getLocation());
        }
        setType(cour);
        return this;
    }


    @Override
    protected void verifyInst(DecacCompiler compiler, EnvironmentExp localEnv,
                              ClassDefinition currentClass, Type returnType)
            throws ContextualError {
        setType(verifyExpr(compiler, localEnv, currentClass));
    }

    /**
     * Verify the expression as a condition, i.e. check that the type is
     * boolean.
     *
     * @param localEnv     Environment in which the condition should be checked.
     * @param currentClass Definition of the class containing the expression, or null in
     *                     the main program.
     */
    protected void verifyCondition(DecacCompiler compiler, EnvironmentExp localEnv,
                                   ClassDefinition currentClass) throws ContextualError {
        Type t = verifyExpr(compiler, localEnv, currentClass);
        if (!t.isBoolean()) {
            throw new ContextualError("Exception : Condition must be boolean", getLocation());
        }

    }

    /**
     * Generate code to print the expression
     *
     * @param compiler
     */
    protected void codeGenPrint(DecacCompiler compiler) {
        // NE DOIT RIEN FAIRE ICI
    }

    @Override
    protected void codeGenInst(DecacCompiler compiler) {
        // throw new UnsupportedOperationException("Not implem");
    }


    @Override
    protected void decompileInst(IndentPrintStream s) {
        if (! isImplicit()) {
            decompile(s);
            if (DecacCompiler.getColor()) {
                s.print(";", "orange");
            } else {
                s.print(";");
            }
        }
    }

    @Override
    protected void prettyPrintType(PrintStream s, String prefix) {
        Type t = getType();
        if (t != null) {
            s.print(prefix);
            s.print("type: ");
            s.print(t);
            s.println();
        }
    }
}
