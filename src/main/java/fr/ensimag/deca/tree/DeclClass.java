package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.*;
import fr.ensimag.deca.tools.IndentPrintStream;
import org.apache.commons.lang.Validate;

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
        Validate.notNull(varName);
        Validate.notNull(varSuper);
        Validate.notNull(listDeclField);
        Validate.notNull(listDeclMethod);
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
        } else if (compiler.environmentType.defOfClass(varSuper.getName()) != compiler.environmentType.defOfClass(compiler.environmentType.OBJECT.getName())) {
            varSuper.setDefinition(compiler.environmentType.defOfClass(varSuper.getName()));

        }
        if (typeDef == null) {
            try {
                environmentType.declareClass(varName.getName(), new ClassDefinition(new ClassType(varName.getName(), this.getLocation(), varSuper.getClassDefinition()), this.getLocation(), varSuper.getClassDefinition()), varSuper.getClassDefinition());
            } catch (EnvironmentExp.DoubleDefException e) {
                throw new ContextualError("Exception : Class " + varName.getName() + " already exists", varName.getLocation());
            }
        }

        Type tName = varName.verifyType(compiler);
        Type tSuper = varSuper.verifyType(compiler);

        varName.setDefinition(new ClassDefinition(new ClassType(varName.getName(), varName.getLocation(), varSuper.getClassDefinition()), varName.getLocation(), varSuper.getClassDefinition()));
        varName.getClassDefinition().setSuperClass(varSuper.getClassDefinition());

        if(varSuper.getClassDefinition().getType() != compiler.environmentType.OBJECT){
            varName.getClassDefinition().setMembers(compiler.environmentExpClass );
        }


        if (tName.getName().getName().equals(tSuper.getName().getName())) {
            throw new ContextualError("Exception : Class name and super class name are the same", varName.getLocation());
        }

    }

    @Override
    protected void verifyClassMembers(DecacCompiler compiler)
            throws ContextualError {
        listDeclField.verifyListDeclField(compiler, varName.getClassDefinition());
        listDeclMethod.verifyListDeclMethod(compiler, varName.getClassDefinition());
    }

    @Override
    protected void verifyClassBody(DecacCompiler compiler) throws ContextualError {
        listDeclMethod.verifyListDeclMethodBody(compiler, varName.getClassDefinition());
        listDeclField.verifyListDeclFieldBody(compiler, varName.getClassDefinition());
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

}
