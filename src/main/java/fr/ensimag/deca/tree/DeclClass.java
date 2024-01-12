package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.Type;
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
        this.varSuper = varSuper; // TODO : si pas de super, varSuper = Object
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
        Type tName = varName.verifyType(compiler);
        Type tSuper = varSuper.verifyType(compiler);
        if (!tName.isString()) {
            throw new ContextualError("Exception : Wrong type for class name", varName.getLocation());
        }

        if (!tSuper.isString()) {
            throw new ContextualError("Exception : Wrong type for class name", varSuper.getLocation());
        }
    }

    @Override
    protected void verifyClassMembers(DecacCompiler compiler)
            throws ContextualError {

    }

    @Override
    protected void verifyClassBody(DecacCompiler compiler) throws ContextualError {
        throw new UnsupportedOperationException("not yet implemented");
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
