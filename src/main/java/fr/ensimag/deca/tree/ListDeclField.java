package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.codegen.codeGen;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.RegisterOffset;
import fr.ensimag.ima.pseudocode.instructions.*;

public class ListDeclField extends TreeList<AbstractDeclField> {
    @Override
    public void decompile(IndentPrintStream s) {
        for (AbstractDeclField f : getList()) {
            f.decompile(s);
        }
    }

    public void verifyListDeclField(DecacCompiler compiler, ClassDefinition currentClass) throws ContextualError {
        for (AbstractDeclField f : getList()) {
            f.verifyField(compiler, currentClass);
        }
    }

    public void verifyListDeclFieldBody(DecacCompiler compiler, ClassDefinition currentClass) throws ContextualError {
        for (AbstractDeclField f : getList()) {
            f.verifyFieldBody(compiler, currentClass);
        }
    }

    public void codeGenFieldPasseTwo(DecacCompiler compiler, ClassDefinition currentClass) {


        compiler.addLabel(new Label("init." + currentClass.getType().getName()));
        // Protect the registers
        codeGen.protect_registres(compiler);

        // On Load les fields de la classe super s'il y en as
        if (currentClass.getSuperClass().getNumberOfFields() != 0) {
           // if (DecacCompiler.getDebug()) {
           //     compiler.addComment("\tChargement des champs hérités de la classe " + currentClass.getSuperClass().getType().getName());
           // }

            compiler.addInstruction(new TSTO(currentClass.getSuperClass().getNumberOfFields() + 1));
            compiler.addInstruction(new BOV(new Label("pile_pleine")));

            compiler.addInstruction(new LOAD(new RegisterOffset(-2, Register.LB), Register.getR(1)));
            compiler.addInstruction(new PUSH(Register.getR(1)));
            compiler.addInstruction(new BSR(compiler.classLabel.addLabel("init." + currentClass.getSuperClass().getType().getName())));
            compiler.addInstruction(new SUBSP(1));
        }

       // if (DecacCompiler.getDebug()) {
       //     compiler.addComment("\tChargement des champs propres de la classe " + currentClass.getType().getName());
       // }


        for (AbstractDeclField f : getList()) {
            f.codeGenFieldPasseTwo(compiler, currentClass);
        }

        // Unprotect the registers
        codeGen.unprotect_registres(compiler);

        compiler.addInstruction(new RTS());
    }
}
