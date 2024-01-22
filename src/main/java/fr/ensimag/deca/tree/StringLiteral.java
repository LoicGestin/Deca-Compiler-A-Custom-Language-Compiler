package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.*;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.IMAProgram;
import fr.ensimag.ima.pseudocode.ImmediateString;
import fr.ensimag.ima.pseudocode.instructions.WNL;
import fr.ensimag.ima.pseudocode.instructions.WSTR;
import org.apache.commons.lang.Validate;

import java.io.PrintStream;

/**
 * String literal
 *
 * @author gl29
 * @date 01/01/2024
 */
public class StringLiteral extends AbstractStringLiteral {

    private final String value;

    public StringLiteral(String value) {
        Validate.notNull(value);
        this.value = value;
    }

    @Override
    public String getValue() {
        return value;
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv,
                           ClassDefinition currentClass) throws ContextualError {
        // Verification of the type and return it
        setType(new StringType(compiler.createSymbol("string")));
        return getType();
    }

    @Override
    protected void codeGenPrint(DecacCompiler compiler) {
        String val = value.substring(1, value.length() - 1).replace("\\\"", "\"").replace("\\\\", "\\");
        // Print the string by splitting it into several WNL and WSTR beetwen \n and not wnl at the end
        String[] split = val.split("\n");
        for (int i = 0; i < split.length; i++) {
            compiler.addInstruction(new WSTR(new ImmediateString(split[i])));
            if (i != split.length - 1) {
                compiler.addInstruction(new WNL());
            }
        }
    }

    @Override
    public void decompile(IndentPrintStream s) {
        if (DecacCompiler.getColor()) {
            s.print(getValue(), "gray");
        } else {
            s.print(getValue());
        }
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        // leaf node => nothing to do
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        // leaf node => nothing to do
    }

    @Override
    String prettyPrintNode() {
        return "StringLiteral (" + value + ")";
    }

    @Override
    protected void codeGenInst(DecacCompiler compiler) {
        throw new UnsupportedOperationException("not Implemented yet");
    }

}
