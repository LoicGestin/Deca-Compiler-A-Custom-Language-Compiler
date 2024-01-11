package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.tools.IndentPrintStream;
import org.apache.commons.lang.Validate;

import java.io.PrintStream;

public class Cast extends AbstractExpr {
    private final AbstractIdentifier type;
    private final AbstractExpr expr;

    public Cast(AbstractIdentifier type, AbstractExpr expr) {
        Validate.notNull(type);
        Validate.notNull(expr);
        this.type = type;
        this.expr = expr;
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv, ClassDefinition currentClass) throws ContextualError {
        Type t = type.verifyType(compiler);
        Type e = expr.verifyExpr(compiler, localEnv, currentClass);
        if (t.isFloat() && e.isInt()) {
            ConvFloat conv = new ConvFloat(expr);
            conv.setType(t);
            conv.setLocation(expr.getLocation());
            setType(t);
            return t;
        }

        throw new ContextualError("Exception : cast error, you can't cast " + t + " to " + e, getLocation());
    }

    @Override
    public void decompile(IndentPrintStream s) {
        s.print("(");
        if (DecacCompiler.getColor()) s.print("\033[35m");
        type.decompile(s);
        if (DecacCompiler.getColor()) s.print("\033[0m");
        s.print(") ");
        s.print("(");
        expr.decompile(s);
        s.print(")");
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        type.prettyPrint(s, prefix, true);
        expr.prettyPrint(s, prefix, true);
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        type.iter(f);
        expr.iter(f);
    }
}
