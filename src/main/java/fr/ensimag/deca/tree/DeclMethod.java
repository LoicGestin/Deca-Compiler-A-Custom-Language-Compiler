package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.codegen.codeGen;
import fr.ensimag.deca.context.*;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.LabelOperand;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.RegisterOffset;
import fr.ensimag.ima.pseudocode.instructions.BOV;
import fr.ensimag.ima.pseudocode.instructions.LOAD;
import fr.ensimag.ima.pseudocode.instructions.STORE;
import fr.ensimag.ima.pseudocode.instructions.TSTO;
import org.apache.commons.lang.Validate;
import org.apache.log4j.Logger;

import java.io.PrintStream;

public class DeclMethod extends AbstractDeclMethod {
    private static final Logger LOG = Logger.getLogger(Main.class);
    private final AbstractIdentifier type;
    private final AbstractIdentifier name;
    private final ListDeclParam params;
    private final EnvironmentExp envParam;
    private final AbstractMethodBody body;
    private boolean isOverride = false;


    public DeclMethod(AbstractIdentifier type, AbstractIdentifier name, ListDeclParam params, AbstractMethodBody body) {
        Validate.notNull(type);
        Validate.notNull(name);
        Validate.notNull(params);
        Validate.notNull(body);
        this.type = type;
        this.name = name;
        this.params = params;
        this.envParam = new EnvironmentExp(null);
        this.body = body;
    }

    public EnvironmentExp getEnvParam() {
        return envParam;
    }
    public MethodDefinition getMethodDefinition(){
        return (MethodDefinition) name.getExpDefinition();
    }

    public boolean isOverride() {
        return isOverride;
    }

    public AbstractIdentifier getName() {
        return name;
    }

    @Override
    public void decompile(IndentPrintStream s) {
        if (DecacCompiler.getColor()) s.print("\033[0;31m");
        type.decompile(s);
        if (DecacCompiler.getColor()) s.print("\033[0m");
        s.print(" ");
        name.decompile(s);
        s.print("(");
        params.decompile(s);
        s.print(") ");
        body.decompile(s);
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        type.prettyPrint(s, prefix, true);
        name.prettyPrint(s, prefix, true);
        params.prettyPrint(s, prefix, true);
        body.prettyPrint(s, prefix, true);
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        type.iter(f);
        name.iter(f);
        params.iter(f);
        body.iter(f);
    }

    @Override
    protected void verifyMethod(DecacCompiler compiler, ClassDefinition currentClass) throws ContextualError {
        LOG.debug("\t[PASSE 2] : \t Méthode " + this.name.getName());

        Type t = type.verifyType(compiler);
        Signature signature = params.verifyListDeclParamMembers(compiler, this.envParam, currentClass);

        int index = currentClass.getNumberOfMethods() + 1;
        EnvironmentExp envSuper = currentClass.getSuperClass().getMembers();

        if(envSuper.get(name.getName()) != null){
            if(envSuper.get(name.getName()).isMethod()){
                MethodDefinition methodDef = (MethodDefinition) envSuper.get(name.getName());
                if(methodDef.getSignature().size() !=(signature.size())){
                    throw new ContextualError("Exception : Signature of method " + name.getName() + " is not the same", name.getLocation());
                }
                if(!compiler.environmentType.subType(compiler.environmentType, t, methodDef.getType())){
                    throw new ContextualError("Exception : Type of method " + name.getName() + " is not the same", name.getLocation());
                }
                isOverride = true;
                index = methodDef.getIndex();
            }
        }

        name.setDefinition(new MethodDefinition(t, name.getLocation(), signature, index));

        if(!isOverride){
            currentClass.incNumberOfMethods();
        }

        try {
            currentClass.getMembers().declare(name.getName(), name.getExpDefinition());
        } catch (EnvironmentExp.DoubleDefException e) {
            throw new ContextualError("Exception : Method " + name.getName() + " already declared", name.getLocation());
        }
        name.verifyExpr(compiler, currentClass.getMembers(), currentClass);
        LOG.debug("\t[PASSE 2] : \t [FIN]");
    }

    @Override
    protected void verifyMethodBody(DecacCompiler compiler, ClassDefinition currentClass) throws ContextualError {
        LOG.debug("\t[PASSE 3] : \t Méthode " + this.name.getName());
        body.verifyMethodBody(compiler, this.envParam, currentClass, type.getType());
        LOG.debug("\t[PASSE 3] : \t [FIN]");
    }

    @Override
    public void codeGenMethodPasseOne(DecacCompiler compiler) {
        Label objectLabel = compiler.classLabel.addLabel("code." + name.getName());
        compiler.addInstruction(new LOAD(new LabelOperand(objectLabel), Register.getR(0)));
        compiler.addInstruction(new STORE(Register.getR(0), new RegisterOffset(codeGen.addIndexPile(), Register.GB)));
    }



    public void codeGenMethodPasseTwo(DecacCompiler compiler) {
        /*

        code.A.m:
        TSTO #d1 ; taille maximale de la pile
        BOV pile_pleine
        ADDSP #d2 ; variables locales
        ; Code de la sauvegarde des registres
        ; Code de la méthode
        ; Message d'erreur si on sort d'une méthode retournant un résultat de type
        ; différent de void autrement que par une instruction return
        ERROR
        fin.A.m:
        ; Code de la restauration des registres
        RTS
         */

        // code.A.m:
        Label objectLabel = compiler.classLabel.addLabel("code." + name.getName());
        compiler.addLabel(objectLabel);

        // TSTO #d1 ; taille maximale de la pile
        compiler.addInstruction(new TSTO(100)); // TODO : taille maximale de la pile

        // BOV pile_pleine
        compiler.addInstruction(new BOV(compiler.getStack_Overflow_error()));

        // ADDSP #d2 ; variables locales
        
    }
}
