package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.codegen.codeGen;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.Type;
import fr.ensimag.ima.pseudocode.instructions.LOAD;
import fr.ensimag.ima.pseudocode.instructions.STORE;

/**
 * Assignment, i.e. lvalue = expr.
 *
 * @author gl29
 * @date 01/01/2024
 */
public class Assign extends AbstractBinaryExpr {

    public Assign(AbstractLValue leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }

    @Override
    public AbstractLValue getLeftOperand() {
        // The cast succeeds by construction, as the leftOperand has been set
        // as an AbstractLValue by the constructor.
        return (AbstractLValue) super.getLeftOperand();
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv,
                           ClassDefinition currentClass) throws ContextualError {
        // Vérification de la compatibilité des types
        Type type_gauche = getLeftOperand().verifyExpr(compiler, localEnv, currentClass);
        Type type_droite = getRightOperand().verifyExpr(compiler, localEnv, currentClass);
        if (compiler.environmentType.compatible(type_gauche, type_droite)) {
            if (type_gauche.isFloat() && type_droite.isInt()) {
                setRightOperand(new ConvFloat(getRightOperand()));
                getRightOperand().verifyExpr(compiler, localEnv, currentClass);
            }
        }
        else{
            throw new ContextualError("Exception : Incompatible types in assign :" + type_gauche + " and " + type_droite, getLocation());
        }
        setType(type_gauche);
        return type_gauche;
    }

    @Override
    protected void codeGenInst(DecacCompiler compiler) {
        codeGen.setAssignation(true);
        getRightOperand().codeGenInst(compiler);
        if (getLeftOperand().isAddr()) {
            compiler.addInstruction(new STORE(codeGen.getRegistreUtilise(), getLeftOperand().getAddr()));
        } else {
            compiler.addInstruction(new LOAD(codeGen.getRegistreUtilise(), getLeftOperand().getGPRegister()));
        }
    }

    @Override
    protected String getOperatorName() {
        return "=";
    }

}
