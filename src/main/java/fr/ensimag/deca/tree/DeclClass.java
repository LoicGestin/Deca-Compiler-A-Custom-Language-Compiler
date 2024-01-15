package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.*;
import fr.ensimag.deca.tools.IndentPrintStream;

import java.io.PrintStream;

/**
 * Declaration of a class (<code>class name extends superClass {members}<code>).
 *
 * @author gl29
 * @date 01/01/2024
 */
public class DeclClass extends AbstractDeclClass {

    private final AbstractIdentifier varName;
    private final AbstractIdentifier varSuper;
    private final ListDeclField listDeclField;
    private final ListDeclMethod listDeclMethod;

    public DeclClass(AbstractIdentifier varName, AbstractIdentifier varSuper, ListDeclField listDeclField, ListDeclMethod listDeclMethod) {
        this.varName = varName;
        this.varSuper = varSuper;
        this.listDeclField = listDeclField;
        this.listDeclMethod = listDeclMethod;
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
        EnvironmentType environmentType = compiler.getEnvironmentType();
        TypeDefinition typeDef = environmentType.defOfType(varName.getName());
        TypeDefinition typeDefSuper = environmentType.defOfType(varSuper.getName());

        if (typeDefSuper == null) {
            throw new ContextualError("Exception : Super class " + varSuper.getName() + " doesn't exist", varSuper.getLocation());
        }
        if (typeDef == null) {
            try {
                environmentType.declareClass(varName.getName(), new ClassDefinition(new ClassType(varName.getName(), varName.getLocation(), varSuper.getClassDefinition()), varName.getLocation(), varSuper.getClassDefinition()));
            } catch (EnvironmentExp.DoubleDefException e) {
                throw new RuntimeException(e);
            }
        }

        Type tName = varName.verifyType(compiler);
        Type tSuper = varSuper.verifyType(compiler);

        if (tName.getName().getName().equals(tSuper.getName().getName())) {
            throw new ContextualError("Exception : Class name and super class name are the same", varName.getLocation());
        }

    }

    @Override
    protected void verifyClassMembers(DecacCompiler compiler)
            throws ContextualError {
        listDeclField.verifyListDeclField(compiler);
        listDeclMethod.verifyListDeclMethod(compiler);
        listDeclMethod.verifyListDeclMethodMembers(compiler);
    }

    @Override
    protected void verifyClassBody(DecacCompiler compiler) throws ContextualError {
        varName.getClassDefinition().setNumberOfFields(listDeclField.getList().size());
        varName.getClassDefinition().setNumberOfMethods(listDeclMethod.getList().size());
        listDeclMethod.verifyListDeclMethodBody(compiler);
        listDeclField.verifyListDeclFieldBody(compiler);
    }


    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        varName.prettyPrint(s, prefix, false);
        varSuper.prettyPrint(s, prefix, false);
        listDeclField.prettyPrint(s, prefix, false);
        listDeclMethod.prettyPrint(s, prefix, false);
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        varName.iter(f);
        varSuper.iter(f);
        listDeclField.iter(f);
        listDeclMethod.iter(f);
    }

}
