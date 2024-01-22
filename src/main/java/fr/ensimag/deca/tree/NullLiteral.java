package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.codegen.codeGen;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.NullOperand;
import fr.ensimag.ima.pseudocode.instructions.LOAD;

import java.io.PrintStream;

public class NullLiteral extends AbstractExpr {
    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv, ClassDefinition currentClass) throws ContextualError {
        setType(compiler.environmentType.NULL);
        return getType();
    }

    @Override
    public void decompile(IndentPrintStream s) {
        if (DecacCompiler.getColor()) {
            s.print("null", "purple");
        } else {
            s.print("null");
        }
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        // leaf node => nothing to do
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        // leaf node => nothing to do
    }

    @Override
    protected void codeGenInst(DecacCompiler compiler) {
        compiler.addInstruction(new LOAD(new NullOperand(), codeGen.getRegistreLibre()));
    }
}
