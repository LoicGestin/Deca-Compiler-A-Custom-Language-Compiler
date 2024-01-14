package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.Type;

public class BinShift extends AbstractBinaryExpr {

    //0 = LEFT; 1 = RIGHT
    private final int direction;

    public BinShift(AbstractExpr leftOp, AbstractExpr rightOp, int direction) {
        super(leftOp, rightOp);
        this.direction = direction;
    }

    public int getDirection() {
        return direction;
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv,
                           ClassDefinition currentClass) throws ContextualError {
        Type typeL = getLeftOperand().verifyExpr(compiler, localEnv, currentClass);
        Type typeR = getRightOperand().verifyExpr(compiler, localEnv, currentClass);

        if (!typeR.isInt()){
            throw new ContextualError("Exception : binary shift value must be int, is " + typeR, getLocation());
        }
        else if (!typeL.isFloat() && !typeL.isInt()){
            throw new ContextualError("Exception : binary shift can only be applied to int or float, not " + typeL, getLocation());
        }

        setType(typeL);
        return getType();
    }

    @Override
    public void codeGenInst(DecacCompiler compiler) {
        throw new UnsupportedOperationException("Binary Shift codeGenInst not yet implemented");
    }

    @Override
    protected String getOperatorName() {
        if (direction == 0) return "<<";
        else return ">>";
    }
}

