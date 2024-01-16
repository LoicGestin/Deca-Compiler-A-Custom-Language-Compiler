package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.codegen.codeGen;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.NullOperand;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.RegisterOffset;
import fr.ensimag.ima.pseudocode.instructions.*;
import fr.ensimag.ima.pseudocode.instructions.HALT;
import org.apache.commons.lang.Validate;
import org.apache.log4j.Logger;

import java.io.PrintStream;

/**
 * Deca complete program (class definition plus main block)
 *
 * @author gl29
 * @date 01/01/2024
 */
public class Program extends AbstractProgram {
    private static final Logger LOG = Logger.getLogger(Program.class);
    private final ListDeclClass classes;
    private final AbstractMain main;

    public Program(ListDeclClass classes, AbstractMain main) {
        Validate.notNull(classes);
        Validate.notNull(main);
        this.classes = classes;
        this.main = main;
    }

    public ListDeclClass getClasses() {
        return classes;
    }

    public AbstractMain getMain() {
        return main;
    }

    @Override
    public void verifyProgram(DecacCompiler compiler) throws ContextualError {
        getClasses().verifyListClass(compiler);
        getClasses().verifyListClassMembers(compiler);
        getClasses().verifyListClassBody(compiler);
        getMain().verifyMain(compiler);
    }

    @Override
    public void codeGenProgram(DecacCompiler compiler) {
        // A FAIRE: compléter ce squelette très rudimentaire de code
        compiler.addComment("start main program");
        /*
        compiler.addInstruction(new LOAD(new NullOperand(), Register.getR(0)));
        compiler.addInstruction(new STORE(Register.getR(0), new RegisterOffset(1, Register.GB)));

        compiler.addInstruction(new LOAD(new NullOperand(), Register.getR(0)));
        compiler.addInstruction(new STORE(Register.getR(0), new RegisterOffset(2, Register.GB)));
        */
        codeGen.setUpRegistres();
        codeGen.genereTopNEntries();
        //compiler.addInstruction(new TSTO(codeGen.tableSize()));
        //compiler.addInstruction(new BOV(compiler.getStack_overflow_error()));
        //compiler.addInstruction(new ADDSP(codeGen.tableSize()));
        main.codeGenMain(compiler);
        compiler.addInstruction(new HALT());
    }

    @Override
    public void decompile(IndentPrintStream s) {
        s.print("// Classes : \n");
        getClasses().decompile(s);
        s.print("// Main :\n");
        getMain().decompile(s);
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        classes.iter(f);
        main.iter(f);
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        classes.prettyPrint(s, prefix, false);
        main.prettyPrint(s, prefix, true);
    }
}
