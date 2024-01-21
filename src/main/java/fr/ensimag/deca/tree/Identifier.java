package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.codegen.codeGen;
import fr.ensimag.deca.context.*;
import fr.ensimag.deca.tools.DecacInternalError;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.deca.tools.SymbolTable.Symbol;
import fr.ensimag.ima.pseudocode.DAddr;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.RegisterOffset;
import fr.ensimag.ima.pseudocode.instructions.LOAD;
import fr.ensimag.ima.pseudocode.instructions.WFLOAT;
import fr.ensimag.ima.pseudocode.instructions.WFLOATX;
import fr.ensimag.ima.pseudocode.instructions.WINT;
import org.apache.commons.lang.Validate;

import java.io.PrintStream;

/**
 * Deca Identifier
 *
 * @author gl29
 * @date 01/01/2024
 */
public class Identifier extends AbstractIdentifier {

    private final Symbol name;
    private Definition definition;

    public Identifier(Symbol name) {
        Validate.notNull(name);
        this.name = name;
    }

    @Override
    protected void checkDecoration() {
        if (getDefinition() == null) {
            throw new DecacInternalError("Identifier " + this.getName() + " has no attached Definition");
        }
    }

    @Override
    public Definition getDefinition() {
        return definition;
    }

    @Override
    public boolean isField(DecacCompiler compiler) {
        return false;
    }

    @Override
    public void setDefinition(Definition definition) {
        this.definition = definition;
    }

    /**
     * Like {@link #getDefinition()}, but works only if the definition is a
     * ClassDefinition.
     * <p>
     * This method essentially performs a cast, but throws an explicit exception
     * when the cast fails.
     *
     * @throws DecacInternalError if the definition is not a class definition.
     */
    @Override
    public ClassDefinition getClassDefinition() {
        try {
            return (ClassDefinition) definition;
        } catch (ClassCastException e) {
            throw new DecacInternalError(
                    "Identifier "
                            + getName()
                            + " is not a class identifier, you can't call getClassDefinition on it");
        }
    }

    /**
     * Like {@link #getDefinition()}, but works only if the definition is a
     * MethodDefinition.
     * <p>
     * This method essentially performs a cast, but throws an explicit exception
     * when the cast fails.
     *
     * @throws DecacInternalError if the definition is not a method definition.
     */
    @Override
    public MethodDefinition getMethodDefinition() {
        try {
            return (MethodDefinition) definition;
        } catch (ClassCastException e) {
            throw new DecacInternalError(
                    "Identifier "
                            + getName()
                            + " is not a method identifier, you can't call getMethodDefinition on it");
        }
    }

    /**
     * Like {@link #getDefinition()}, but works only if the definition is a
     * FieldDefinition.
     * <p>
     * This method essentially performs a cast, but throws an explicit exception
     * when the cast fails.
     *
     * @throws DecacInternalError if the definition is not a field definition.
     */
    @Override
    public FieldDefinition getFieldDefinition() {
        try {
            return (FieldDefinition) definition;
        } catch (ClassCastException e) {
            throw new DecacInternalError(
                    "Identifier "
                            + getName()
                            + " is not a field identifier, you can't call getFieldDefinition on it");
        }
    }

    /**
     * Like {@link #getDefinition()}, but works only if the definition is a
     * VariableDefinition.
     * <p>
     * This method essentially performs a cast, but throws an explicit exception
     * when the cast fails.
     *
     * @throws DecacInternalError if the definition is not a field definition.
     */
    @Override
    public VariableDefinition getVariableDefinition() {
        try {
            return (VariableDefinition) definition;
        } catch (ClassCastException e) {
            throw new DecacInternalError(
                    "Identifier "
                            + getName()
                            + " is not a variable identifier, you can't call getVariableDefinition on it");
        }
    }

    /**
     * Like {@link #getDefinition()}, but works only if the definition is a ExpDefinition.
     * <p>
     * This method essentially performs a cast, but throws an explicit exception
     * when the cast fails.
     *
     * @throws DecacInternalError if the definition is not a field definition.
     */
    @Override
    public ExpDefinition getExpDefinition() {
        try {
            return (ExpDefinition) definition;
        } catch (ClassCastException e) {
            throw new DecacInternalError(
                    "Identifier "
                            + getName()
                            + " is not a Exp identifier, you can't call getExpDefinition on it");
        }
    }

    @Override
    public Symbol getName() {
        return name;
    }

    @Override
    public DAddr getAddr(DecacCompiler compiler) {
        return this.getExpDefinition().getOperand();
    }

    @Override
    public GPRegister getGPRegister() {
        return this.getExpDefinition().getGPRegister();
    }

    @Override
    public boolean isAddr(DecacCompiler compiler) {
        return this.getExpDefinition().isAddr();
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv,
                           ClassDefinition currentClass) throws ContextualError {
        ExpDefinition expDef = localEnv.get(this.getName());
        codeGen.addVariableTable(this.getName().toString());
        if (expDef == null && currentClass != null) {
            expDef = currentClass.getMembers().get(this.getName());
        }

        if (expDef == null && currentClass == null) {
            ClassDefinition exp = compiler.environmentType.defOfClass(this.getName());
            if(exp != null){
                setDefinition(exp);
                setType(exp.getType());
                return exp.getType();
            }
            else {
                throw new ContextualError("Exception : Identifier " + this.getName() + " is not defined", this.getLocation());
            }
        }

        if(expDef == null){
            throw new ContextualError("Exception : Identifier " + this.getName() + " is not defined", this.getLocation());
        }


        setDefinition(expDef);
        setType(expDef.getType());

        return expDef.getType();
    }

    /**
     * Implements non-terminal "type" of [SyntaxeContextuelle] in the 3 passes
     *
     * @param compiler contains "env_types" attribute
     */
    @Override
    public Type verifyType(DecacCompiler compiler) throws ContextualError {
        TypeDefinition typeDef = compiler.environmentType.defOfType(this.getName());
        if (typeDef == null)
            throw new ContextualError("Exception : Type " + this.getName() + " is not defined", this.getLocation());

        if (!typeDef.getType().isClass()) {
            setDefinition(typeDef);
        } else {
            ClassDefinition classDef = compiler.environmentType.getClassDefinition(this.getName());
            setDefinition(classDef);
        }

        setType(typeDef.getType());

        return typeDef.getType();
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        // leaf node => nothing to do
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        // leaf node => nothing to do
    }

    @Override
    public void decompile(IndentPrintStream s) {
        s.print(name.toString());
    }

    @Override
    String prettyPrintNode() {
        return "Identifier (" + getName() + ")";
    }

    @Override
    protected void prettyPrintType(PrintStream s, String prefix) {
        Definition d = getDefinition();
        if (d != null) {
            s.print(prefix);
            s.print("definition: ");
            s.print(d);
            s.println();
        }
    }

    @Override
    protected void codeGenPrint(DecacCompiler compiler) {
        if (definition.getType().isBoolean()) {
            compiler.addInstruction(new LOAD(this.getExpDefinition().isAddr() ? this.getExpDefinition().getOperand() : this.getExpDefinition().getGPRegister(), codeGen.getRegistreLibre()));
            print_boolean(compiler);
        } else {
            compiler.addInstruction(new LOAD(this.getExpDefinition().isAddr() ? this.getExpDefinition().getOperand() : this.getExpDefinition().getGPRegister(), codeGen.getCurrentRegistreLibre()));
            compiler.addInstruction(new LOAD(codeGen.getCurrentRegistreLibre(), GPRegister.getR(1)));
            if (definition.getType().isInt()) {
                compiler.addInstruction(new WINT());
            } else if (definition.getType().isFloat()) {
                compiler.addInstruction(super.isHexa() ? new WFLOATX() : new WFLOAT());
            } else {
                throw new UnsupportedOperationException("Not supported yet.");
            }
        }
    }

    @Override
    protected void codeGenInst(DecacCompiler compiler) {
        if (this.getExpDefinition().isField()) {
            // Generate the code for the field
            compiler.addInstruction(new LOAD(new RegisterOffset(-2, Register.LB), Register.R1));
        }
        codeGen.setRegistreCourant(this.getExpDefinition().isAddr() ? this.getExpDefinition().getOperand() : this.getExpDefinition().getGPRegister(), compiler);
    }
}
