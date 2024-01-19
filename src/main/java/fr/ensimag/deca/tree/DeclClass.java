package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.codegen.codeGen;
import fr.ensimag.deca.context.*;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.deca.tools.SymbolTable;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.LabelOperand;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.RegisterOffset;
import fr.ensimag.ima.pseudocode.instructions.LEA;
import fr.ensimag.ima.pseudocode.instructions.LOAD;
import fr.ensimag.ima.pseudocode.instructions.STORE;
import org.apache.commons.lang.Validate;
import org.apache.log4j.Logger;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Declaration of a class (<code>class name extends superClass {members}<code>).
 *
 * @author gl29
 * @date 01/01/2024
 */
public class DeclClass extends AbstractDeclClass {

    private static final Logger LOG = Logger.getLogger(Main.class);
    private final AbstractIdentifier varName;
    private final AbstractIdentifier varSuper;
    private final ListDeclField listDeclField;
    private final ListDeclMethod listDeclMethod;

    public DeclClass(AbstractIdentifier varName, AbstractIdentifier varSuper, ListDeclField listDeclField, ListDeclMethod listDeclMethod) {
        Validate.notNull(varName);
        Validate.notNull(varSuper);
        Validate.notNull(listDeclField);
        Validate.notNull(listDeclMethod);
        this.varName = varName;
        this.varSuper = varSuper;
        this.listDeclField = listDeclField;
        this.listDeclMethod = listDeclMethod;


    }

    public String getClassName() {
        return varName.getName().getName();
    }

    public AbstractIdentifier getVarSuper() {
        return varSuper;
    }

    public ListDeclMethod getListDeclMethod() {
        return listDeclMethod;
    }

    @Override
    public void decompile(IndentPrintStream s) {
        if (DecacCompiler.getColor()) {
            s.print("class ", "purple");
            varName.decompile(s);
            if (varSuper != null) {
                s.print(" extends ", "purple");
                varSuper.decompile(s);
            }

        } else {
            s.print("class ");
            varName.decompile(s);
            if (varSuper != null) {
                s.print(" extends ");
                varSuper.decompile(s);
            }
        }

        s.println(" {");
        s.indent();
        listDeclField.decompile(s);
        listDeclMethod.decompile(s);
        s.unindent();
        s.println("}");
    }

    @Override
    protected void verifyClass(DecacCompiler compiler) throws ContextualError {

        LOG.debug("[PASSE 1] : Classe " + this.varName.getName());

        EnvironmentType environmentType = compiler.getEnvironmentType();
        ClassDefinition typeDefSuper = environmentType.defOfClass(varSuper.getName());
        if (typeDefSuper == null) {
            throw new ContextualError("Exception : Super class " + varSuper.getName() + " doesn't exist", varSuper.getLocation());
        }

        varSuper.setDefinition(typeDefSuper);

        try {
            environmentType.declareClass(varName.getName(), new ClassDefinition(new ClassType(varName.getName(), varName.getLocation(), varSuper.getClassDefinition()), varName.getLocation(), varSuper.getClassDefinition()), varSuper.getClassDefinition());
        } catch (EnvironmentExp.DoubleDefException e) {
            throw new ContextualError("Exception : Class " + varName.getName() + " already exists", varName.getLocation());
        }

        varName.setDefinition(new ClassDefinition(new ClassType(varName.getName(), varName.getLocation(), varSuper.getClassDefinition()), varName.getLocation(), varSuper.getClassDefinition()));

        varName.verifyType(compiler);
        varSuper.verifyType(compiler);

        LOG.debug("[PASSE 1] : [FIN]");
    }

    @Override
    protected void verifyClassMembers(DecacCompiler compiler)
            throws ContextualError {
        LOG.debug("[PASSE 2] : Classe " + this.varName.getName());
        varName.getClassDefinition().setNumberOfFields(varSuper.getClassDefinition().getNumberOfFields());
        listDeclField.verifyListDeclField(compiler, varName.getClassDefinition());
        listDeclMethod.verifyListDeclMethod(compiler, varName.getClassDefinition());
        LOG.debug("[PASSE 2] : [FIN]");
    }

    @Override
    protected void verifyClassBody(DecacCompiler compiler) throws ContextualError {
        LOG.debug("[PASSE 3] : Classe " + this.varName.getName());

        listDeclField.verifyListDeclFieldBody(compiler, varName.getClassDefinition());
        listDeclMethod.verifyListDeclMethodBody(compiler, varName.getClassDefinition());
        LOG.debug("[PASSE 3] : [FIN]");
    }


    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        varName.prettyPrint(s, prefix, false);
        varSuper.prettyPrint(s, prefix, false);
        listDeclField.prettyPrint(s, prefix, false);
        listDeclMethod.prettyPrint(s, prefix, true);
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        varName.iter(f);
        varSuper.iter(f);
        listDeclField.iter(f);
        listDeclMethod.iter(f);
    }

    @Override
    public void codeGenClassPasseOne(DecacCompiler compiler) {
        LOG.trace("Ecriture du code de la table des méthodes de " + varName.getName());
        compiler.addComment("Code de la table des méthodes de " + varName.getName() + ";");
        varName.getClassDefinition().setAdressTable(codeGen.getIndexPile());
        compiler.addInstruction(new LEA(new RegisterOffset( varSuper.getName().getName().equals("Object") ? 1 : varSuper.getClassDefinition().getAdressTable(), Register.GB), Register.getR(0)));
        compiler.addInstruction(new STORE(Register.getR(0), new RegisterOffset(codeGen.addIndexPile(), Register.GB)));

        Map<Integer,String> hashMap = codeGen.getTableDesMethodes(getClassName());
        for (Map.Entry<Integer, String> entry : hashMap.entrySet()) {
            compiler.addInstruction(new LOAD(new LabelOperand(compiler.classLabel.addLabel(entry.getValue())), Register.getR(0)));
            compiler.addInstruction(new STORE(Register.getR(0), new RegisterOffset(codeGen.addIndexPile(), Register.GB)));
        }
    }

    @Override
    public void codeGenClassPasseTwo(DecacCompiler compiler) {

        compiler.addComment("Code de la classe " + varName.getName() + ";");
        Label objectLabel = compiler.classLabel.addLabel("init." + varName.getName());
        if (DecacCompiler.getDebug()){
            compiler.addComment("Initialisation des champs de " + varName.getName());
        }
        // init.A
        compiler.addLabel(objectLabel);
        listDeclField.codeGenFieldPasseTwo(compiler, varName.getClassDefinition());
        listDeclMethod.codeGenListDeclMethod(compiler, varName.getClassDefinition());

    }
}
