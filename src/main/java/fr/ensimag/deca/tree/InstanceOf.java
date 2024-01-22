package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.codegen.codeGen;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.Type;
import fr.ensimag.ima.pseudocode.*;
import fr.ensimag.ima.pseudocode.instructions.*;

public class InstanceOf extends AbstractOpExactCmp {
    public InstanceOf(AbstractExpr leftOperand, AbstractIdentifier rightOperand) {
        super(leftOperand, rightOperand);
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv, ClassDefinition currentClass) throws ContextualError {
        Type t1 = this.getLeftOperand().verifyExpr(compiler, localEnv, currentClass);
        Type t2 = this.getRightOperand().verifyExpr(compiler, localEnv, currentClass);
        if (!(t1.isClassOrNull() && t2.isClass())) {
            throw new ContextualError("Exception : type incompatible : " + t1 + " and " + t2, getLeftOperand().getLocation());
        }

        setType(compiler.environmentType.BOOLEAN);
        return getType();
    }

    @Override
    public void codeGenOp(DecacCompiler compiler) {
    }

    @Override
    public void codeGenInst(DecacCompiler compiler) {
        // a InstanceOf B
        // On parcours la chaine de pointeur

        // On récupère l'addresse de la classe de l'operande gauche

        Label fin = compiler.labelTable.addLabel("fin_instanceof");
        Label deb = compiler.labelTable.addLabel("debut_instanceof");
        Label not_eq = compiler.labelTable.addLabel("noteq_instanceof");

        getLeftOperand().codeGenInst(compiler);

        GPRegister r = codeGen.getCurrentRegistreUtilise();

        compiler.addInstruction(new LEA(new RegisterOffset(compiler.getEnvironmentType().getClassDefinition(getRightOperand().getType().getName()).getAdressTable(), Register.GB), Register.R0));

        compiler.addLabel(deb);

        // On teste si le parametre implicite est null
        compiler.addInstruction(new CMP(new RegisterOffset(1, Register.GB), r));
        compiler.addInstruction(new BEQ(not_eq));
        compiler.addInstruction(new LOAD(new RegisterOffset(0, r), r));
        compiler.addInstruction(new CMP(r, Register.R0));
        compiler.addInstruction(new BNE(deb));
        compiler.addInstruction(new SEQ(r));
        compiler.addInstruction(new BRA(fin));
        compiler.addLabel(not_eq);
        compiler.addInstruction(new LOAD(new ImmediateInteger(0), codeGen.getCurrentRegistreUtilise()));
        compiler.addLabel(fin);
    }


    @Override
    protected String getOperatorName() {
        return "instanceof";
    }
}
