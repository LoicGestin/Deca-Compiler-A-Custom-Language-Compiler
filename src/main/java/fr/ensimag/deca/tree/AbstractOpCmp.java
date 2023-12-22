package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.*;
import fr.ensimag.deca.DecacCompiler;

/**
 *
 * @author gl29
 * @date 01/01/2024
 */
public abstract class AbstractOpCmp extends AbstractBinaryExpr {

    public AbstractOpCmp(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv,
            ClassDefinition currentClass) throws ContextualError {
        this.getLeftOperand().verifyExpr(compiler, localEnv, currentClass);
        this.getRightOperand().verifyExpr(compiler, localEnv, currentClass);
        Type t1 =this.getLeftOperand().getType();
        Type t2 =this.getRightOperand().getType();
      if (t1.sameType(t2)){
        if (t1.isInt() || t1.isFloat() || t1.isBoolean()){
          this.setType(new BooleanType(compiler.createSymbol("boolean")));
          return this.getType();
        }
        else{
          throw new ContextualError("Exception : type incompatible", this.getLocation());
        }
      }
      else{
        throw new ContextualError("Exception : type incompatible", this.getLocation());
      }
    }


}
