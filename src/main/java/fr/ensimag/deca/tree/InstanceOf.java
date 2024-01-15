package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;

public class InstanceOf extends AbstractOpExactCmp {
    public InstanceOf(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }

    @Override
    public void codeGenOp(DecacCompiler compiler) {
        throw new UnsupportedOperationException("not yet implemented");
    }

    @Override
    protected String getOperatorName() {
        return "instanceof";
    }
}
