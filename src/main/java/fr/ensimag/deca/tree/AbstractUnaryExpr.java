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

        if (super.getType().isBoolean()) {
            print_boolean(compiler);
        } else {
            compiler.addInstruction(new LOAD(codeGen.getCurrentRegistreUtilise(), GPRegister.getR(1)));
            if (super.getType().isInt()) {
                compiler.addInstruction(new WINT());
            } else if (super.getType().isFloat()) {
                compiler.addInstruction(super.isHexa() ? new WFLOATX() : new WFLOAT());
            }
        }
    }

    public abstract void codeGenOp(DecacCompiler compiler);

    @Override
    public void codeGenInst(DecacCompiler compiler) {
        codeGen.setAssignation(true);
        getOperand().codeGenInst(compiler);
        codeGenOp(compiler);
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
