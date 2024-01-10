package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.*;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.instructions.RINT;
import java.io.PrintStream;

/**
 * @author gl29
 * @date 01/01/2024
 */
public class ReadInt extends AbstractReadExpr {

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv,
                           ClassDefinition currentClass) throws ContextualError {
        setType(new IntType(compiler.createSymbol("int")));
        return getType();
    }


    @Override
    public void decompile(IndentPrintStream s) {
        if (DecacCompiler.getColor()) {
            s.print("readInt", "purple");
            s.print("()");
        } else {
            s.print("readInt()");
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

    public void codeGenInst(DecacCompiler compiler) {
        compiler.addInstruction(new RINT());
    }

}
