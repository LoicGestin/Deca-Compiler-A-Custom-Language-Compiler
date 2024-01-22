package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.codegen.codeGen;
import fr.ensimag.deca.context.*;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.*;
import fr.ensimag.ima.pseudocode.instructions.LOAD;
import fr.ensimag.ima.pseudocode.instructions.STORE;
import org.apache.commons.lang.Validate;
import org.apache.log4j.Logger;

import java.io.PrintStream;

public class DeclField extends AbstractDeclField {
    private static final Logger LOG = Logger.getLogger(Main.class);
    private final AbstractIdentifier type;
    private final AbstractIdentifier field;
    private final AbstractInitialization initialization;
    private final Visibility visibility;

    public DeclField(AbstractIdentifier type, AbstractIdentifier field, AbstractInitialization initialization, Visibility visibility) {
        Validate.notNull(type);
        Validate.notNull(field);
        Validate.notNull(initialization);
        Validate.notNull(visibility);
        this.type = type;
        this.field = field;
        this.initialization = initialization;
        this.visibility = visibility;
    }

    protected static void print_def_variable(IndentPrintStream s, AbstractIdentifier type, AbstractIdentifier field, AbstractInitialization initialization) {
        if (DecacCompiler.getColor()) s.print("\033[0;31m");
        type.decompile(s);
        if (DecacCompiler.getColor()) s.print("\033[0m");
        s.print(" ");
        field.decompile(s);
        if (!(initialization instanceof NoInitialization)) {
            s.print(" = ");
            initialization.decompile(s);
        }
    }

    @Override
    public void decompile(IndentPrintStream s) {
        if (DecacCompiler.getColor()) {
            if (visibility == Visibility.PROTECTED) {
                s.print("protected ", "purple");
            }

        } else {
            if (visibility == Visibility.PROTECTED) {
                s.print("protected ");
            }
        }
        print_def_variable(s, type, field, initialization);
        if (DecacCompiler.getColor()) {
            s.print(";", "orange");
        } else {
            s.println(";");
        }
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        type.prettyPrint(s, prefix, true);
        field.prettyPrint(s, prefix, true);
        initialization.prettyPrint(s, prefix, true);
    }

    @Override
    protected String prettyPrintNode() {
        if (visibility == Visibility.PROTECTED) {
            return "[visibility=PROTECTED] DeclField";
        }
        return "";

    }

    @Override
    protected void iterChildren(TreeFunction f) {
        type.iter(f);
        field.iter(f);
        initialization.iter(f);
    }

    @Override
    protected void verifyField(DecacCompiler compiler, ClassDefinition currentClass) throws ContextualError {
        LOG.debug("\t[PASSE 2] : \t Field");
        Type t = type.verifyType(compiler);
        if (t.isVoid()) {
            throw new ContextualError("Exception : Variable type cannot be void", type.getLocation());
        }

        EnvironmentExp envSuper = currentClass.getSuperClass().getMembers();

        if (envSuper.get(field.getName()) != null) {
            if (envSuper.get(field.getName()).isMethod()) {
                throw new ContextualError("Exception : Field " + field.getName() + " is already defined as a method in SuperClass", field.getLocation());
            }
        }

        field.setDefinition(new FieldDefinition(t, field.getLocation(), visibility, currentClass, currentClass.getNumberOfFields() + 1));

        try {
            currentClass.getMembers().declare(field.getName(), field.getFieldDefinition());
        } catch (EnvironmentExp.DoubleDefException e) {
            throw new ContextualError("Exception : Field " + field.getName() + " is already defined", field.getLocation());
        }

        field.verifyExpr(compiler, currentClass.getMembers(), currentClass);
        currentClass.incNumberOfFields();
        LOG.debug("\t[PASSE 2] : \t [FIN]");
    }


    protected void verifyFieldBody(DecacCompiler compiler, ClassDefinition currentClass) throws ContextualError {
        LOG.debug("\t[PASSE 3] : \t Field");
        initialization.verifyInitialization(compiler, field.getType(), currentClass.getMembers(), currentClass);
        LOG.debug("\t[PASSE 3] : \t [FIN]");
    }

    @Override
    public void codeGenFieldPasseTwo(DecacCompiler compiler, ClassDefinition classDefinition) {

        FieldDefinition fieldDefinition = field.getFieldDefinition();
        fieldDefinition.setOperand(new RegisterOffset(fieldDefinition.getIndex(), Register.R1));

        // Generate the code for the field
        compiler.addInstruction(new LOAD(new RegisterOffset(-2, Register.LB), Register.R1));

        if (initialization instanceof NoInitialization) {
            if (field.getType().isInt() || field.getType().isBoolean()) {
                compiler.addInstruction(new LOAD(new ImmediateInteger(0), codeGen.getRegistreLibre()));
            } else if (field.getType().isFloat()) {
                compiler.addInstruction(new LOAD(new ImmediateFloat(0), codeGen.getRegistreLibre()));
            } else if (field.getType().isClass()) {
                compiler.addInstruction(new LOAD(new NullOperand(), codeGen.getRegistreLibre()));
            }
            compiler.addInstruction(new STORE(codeGen.getRegistreUtilise(), fieldDefinition.getOperand()));
        }
        // Generate the code for the initialization
        initialization.codeGenInit(compiler, field.getFieldDefinition());
    }

}
