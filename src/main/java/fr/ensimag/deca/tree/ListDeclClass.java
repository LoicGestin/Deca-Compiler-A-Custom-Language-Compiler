package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.codegen.codeGen;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.*;
import fr.ensimag.ima.pseudocode.instructions.LOAD;
import fr.ensimag.ima.pseudocode.instructions.STORE;
import org.apache.log4j.Logger;

/**
 * @author gl29
 * @date 01/01/2024
 */
public class ListDeclClass extends TreeList<DeclClass> {
    private static final Logger LOG = Logger.getLogger(ListDeclClass.class);

    @Override
    public void decompile(IndentPrintStream s) {
        for (AbstractDeclClass c : getList()) {
            c.decompile(s);
            s.println();
        }
    }

    /**
     * Pass 1 of [SyntaxeContextuelle]
     */
    void verifyListClass(DecacCompiler compiler) throws ContextualError {
        LOG.debug("verify listClass: start");
        for (AbstractDeclClass c : getList()) {
            c.verifyClass(compiler);
        }
        LOG.debug("verify listClass: end");
    }

    /**
     * Pass 2 of [SyntaxeContextuelle]
     */
    public void verifyListClassMembers(DecacCompiler compiler) throws ContextualError {
        for (AbstractDeclClass c : getList()) {
            c.verifyClassMembers(compiler);
        }
    }

    /**
     * Pass 3 of [SyntaxeContextuelle]
     */
    public void verifyListClassBody(DecacCompiler compiler) throws ContextualError {
        for (AbstractDeclClass c : getList()) {
            c.verifyClassBody(compiler);
        }
    }

    public void codeGenListClassPasseOne(DecacCompiler compiler){
        compiler.addComment("Code de la table des méthodes de Object;");
        compiler.addInstruction(new LOAD(new NullOperand(), Register.getR(0)));
        compiler.addInstruction(new STORE(Register.getR(0), new RegisterOffset(1, Register.GB)));
        Label objectLabel = compiler.labelTable.addLabel("code.Object.equals");
        codeGen.setObjectLabel(objectLabel);
        compiler.addInstruction(new LOAD(new LabelOperand(codeGen.getObjectLabel()), Register.getR(0)));
        compiler.addInstruction(new STORE(Register.getR(0), new RegisterOffset(2, Register.GB)));
        for (AbstractDeclClass c : getList()) {
            c.codeGenClassPasseOne(compiler);
        }
    }
    public void codeGenListClassPasseTwo(DecacCompiler compiler){

    }


}
