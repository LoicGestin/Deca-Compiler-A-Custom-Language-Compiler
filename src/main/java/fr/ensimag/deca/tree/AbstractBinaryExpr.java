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
 * Binary expressions.
 *
 * @author gl29
 * @date 01/01/2024
 */
public abstract class AbstractBinaryExpr extends AbstractExpr {

    private AbstractExpr leftOperand;
    private AbstractExpr rightOperand;

    public AbstractBinaryExpr(AbstractExpr leftOperand,
                              AbstractExpr rightOperand) {
        Validate.notNull(leftOperand, "left operand cannot be null");
        Validate.notNull(rightOperand, "right operand cannot be null");
        //Validate.isTrue(leftOperand != rightOperand, "Sharing subtrees is forbidden");
        this.leftOperand = leftOperand;
        this.rightOperand = rightOperand;
    }

    public AbstractExpr getLeftOperand() {
        return leftOperand;
    }

    protected void setLeftOperand(AbstractExpr leftOperand) {
        Validate.notNull(leftOperand);
        this.leftOperand = leftOperand;
    }

    public AbstractExpr getRightOperand() {
        return rightOperand;
    }

    protected void setRightOperand(AbstractExpr rightOperand) {
        Validate.notNull(rightOperand);
        this.rightOperand = rightOperand;
    }

    @Override
    public void decompile(IndentPrintStream s) {
        // If operator is =, no need parenthesis
        if (getOperatorName().equals("=")) {
            leftOperand.decompile(s);
            s.print(" " + getOperatorName() + " ");
            rightOperand.decompile(s);
        } else {
            s.print("(");
            leftOperand.decompile(s);
            s.print(" " + getOperatorName() + " ");
            rightOperand.decompile(s);
            s.print(")");
        }
    }

    public void codeGenPrint(DecacCompiler compiler) {
        codeGenInst(compiler);
        if (super.getType().isBoolean()) {
            print_boolean(compiler);
        } else {
            compiler.addInstruction(new LOAD(codeGen.getRegistreUtilise(), GPRegister.getR(1)));
            if (super.getType().isInt()) {
                compiler.addInstruction(new WINT());
            } else if (super.getType().isFloat()) {
                compiler.addInstruction(super.isHexa() ? new WFLOATX() : new WFLOAT());
            }
        }
    }

    abstract protected String getOperatorName();

    @Override
    protected void iterChildren(TreeFunction f) {
        leftOperand.iter(f);
        rightOperand.iter(f);
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        leftOperand.prettyPrint(s, prefix, false);
        rightOperand.prettyPrint(s, prefix, true);
    }

    @Override
    protected abstract void codeGenInst(DecacCompiler compiler);
}
