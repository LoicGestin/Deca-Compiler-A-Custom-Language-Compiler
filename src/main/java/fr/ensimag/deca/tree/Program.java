package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.codegen.codeGen;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.RegisterOffset;
import fr.ensimag.ima.pseudocode.instructions.*;
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


        // PASSE 1
        //— construction du tableau des étiquettes des méthodes.
        //— génération de code permettant de construire la table des méthodes.
        codeGen.construireTableDesMethodes(classes.getList());
        codeGen.init_registres(compiler);
        classes.codeGenListClassPasseOne(compiler);


        compiler.addComment("start main program");
        LOG.trace("start main program");
        // PASSE 2
        //— codage du programme principal (déclarations et instructions)
        //— codage des champs de chaque classe (initialisation) ;
        //— codage des méthodes de chaque classe (déclarations et instructions) ;

        main.codeGenMain(compiler);
        compiler.addInstruction(new HALT());
        compiler.addComment("end main program");
        LOG.trace("end main program");

        codeGen.clear_registres();

        classes.codeGenListClassPasseTwo(compiler);


        if (!DecacCompiler.getNocheck()) {
            compiler.addLabel(new Label("overflow_error"));
            compiler.addInstruction(new WSTR("Error: Overflow during arithmetic operation"));
            compiler.addInstruction(new WNL());
            compiler.addInstruction(new ERROR());

            compiler.addLabel(new Label("io_error"));
            compiler.addInstruction(new WSTR("Error: Input/Output error"));
            compiler.addInstruction(new WNL());
            compiler.addInstruction(new ERROR());

            compiler.addLabel(new Label("pile_pleine"));
            compiler.addInstruction(new WSTR("Error: Stack overflow"));
            compiler.addInstruction(new WNL());
            compiler.addInstruction(new ERROR());

            compiler.addLabel(new Label("dereferencement.null"));
            compiler.addInstruction(new WSTR("Error: deferencement of null pointer"));
            compiler.addInstruction(new WNL());
            compiler.addInstruction(new ERROR());

        }
        compiler.addLabel(new Label("code.object.equals"));
        compiler.addInstruction(new LOAD(new RegisterOffset(-2, Register.LB), Register.getR(0)));
        compiler.addInstruction(new LOAD(new RegisterOffset(-3, Register.LB), Register.getR(1)));
        compiler.addInstruction(new CMP(Register.getR(1), Register.getR(0)));
        compiler.addInstruction(new SEQ(Register.getR(0)));
        compiler.addInstruction(new RTS());


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
