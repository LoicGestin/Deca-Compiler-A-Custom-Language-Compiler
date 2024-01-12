package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.codegen.codeGen;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.instructions.LOAD;
import fr.ensimag.ima.pseudocode.instructions.WFLOAT;
import fr.ensimag.ima.pseudocode.instructions.WFLOATX;
import fr.ensimag.ima.pseudocode.instructions.WINT;
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

    public void codeGenPrint(DecacCompiler compiler) {
        codeGenInst(compiler);
        // On charge le registre contenant la valeur à afficher
        compiler.addInstruction(new LOAD(codeGen.getCurrentRegistreUtilise(), GPRegister.getR(1)));
        // On affiche la valeur en fonction de son type
        if (super.getType().isInt()) {
            compiler.addInstruction(new WINT());
        } else if (super.getType().isFloat()) {
            compiler.addInstruction(super.isHexa() ? new WFLOATX() : new WFLOAT());
        } else if (super.getType().isBoolean()) {
            print_boolean(compiler);
        } else {
            throw new UnsupportedOperationException("Not supported yet.");
        }
    }

    @Override
    public void decompile(IndentPrintStream s) {
        s.print('(' + getOperatorName() + operand.decompile() + ')');
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
