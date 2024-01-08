package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.tools.IndentPrintStream;
import net.bytebuddy.implementation.bytecode.Throw;
import org.apache.commons.lang.Validate;

import java.io.PrintStream;

public class New extends AbstractExpr{
    private AbstractIdentifier type;
    public New(AbstractIdentifier type){
        Validate.notNull(type);
        this.type = type;
    }

    @Override
    public void decompile(IndentPrintStream s) {
        s.print("\033[0;35mnew\033[0m ");
        type.decompile(s);
        s.print("()");
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

    }

    /**
     * Function used internally by {@link #iter(TreeFunction)}. Must call iter() on each
     * child of the tree.
     *
     * @param f
     */
    @Override
    protected void iterChildren(TreeFunction f) {

    }


    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv, ClassDefinition currentClass) throws ContextualError {
        throw new UnsupportedOperationException("not yet implemented");
    }
}
