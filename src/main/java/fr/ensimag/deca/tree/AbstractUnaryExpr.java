package fr.ensimag.deca.tree;

import fr.ensimag.deca.tools.IndentPrintStream;
import org.apache.commons.lang.Validate;

import java.io.PrintStream;

/**
 * Unary expression.
 *
 * @author gl29
 * @date 01/01/2024
 */
public abstract class AbstractUnaryExpr extends AbstractExpr {

    private final AbstractExpr operand;

    public AbstractUnaryExpr(AbstractExpr operand) {
        Validate.notNull(operand);
        this.operand = operand;
    }

    public AbstractExpr getOperand() {
        return operand;
    }

    protected abstract String getOperatorName();

    @Override
    public void decompile(IndentPrintStream s) {
        s.print("(" + getOperatorName() + operand.decompile() + ")");
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        operand.iter(f);
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        operand.prettyPrint(s, prefix, true);
    }

}
