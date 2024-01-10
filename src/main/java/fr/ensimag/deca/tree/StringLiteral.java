package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.*;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.ImmediateInteger;
import fr.ensimag.ima.pseudocode.ImmediateString;
import fr.ensimag.ima.pseudocode.instructions.LOAD;
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

    private String value;

    public StringLiteral(String value) {
        Validate.notNull(value);
        this.value = value.replace("\"", "");
    }

    private static int stringToAsciiInt(String input) {
        StringBuilder asciiStringBuilder = new StringBuilder();

        for (int i = 0; i < input.length(); i++) {
            char c = input.charAt(i);
            asciiStringBuilder.append((int) c);
        }
        return Integer.parseInt(asciiStringBuilder.toString());
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
        compiler.addInstruction(new WSTR(new ImmediateString(value)));
    }

    @Override
    public void codeGenInst(DecacCompiler compiler) {
        //TO DO
        compiler.addInstruction(new LOAD(new ImmediateInteger(stringToAsciiInt(value)), compiler.getRegister(2)));
    }

    @Override
    public void decompile(IndentPrintStream s) {
        if (DecacCompiler.getColor()) {
            s.print("\"" + value + "\"", "gray");
        } else {
            s.print("\"" + value + "\"");
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

}
