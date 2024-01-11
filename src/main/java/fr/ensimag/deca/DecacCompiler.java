package fr.ensimag.deca;

import fr.ensimag.deca.context.EnvironmentType;
import fr.ensimag.deca.syntax.DecaLexer;
import fr.ensimag.deca.syntax.DecaParser;
import fr.ensimag.deca.tools.DecacInternalError;
import fr.ensimag.deca.tools.LabelTable;
import fr.ensimag.deca.tools.SymbolTable;
import fr.ensimag.deca.tools.SymbolTable.Symbol;
import fr.ensimag.deca.tree.AbstractProgram;
import fr.ensimag.deca.tree.LocationException;
import fr.ensimag.ima.pseudocode.*;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.apache.log4j.Logger;

import java.io.*;

/**
 * Decac compiler instance.
 * <p>
 * This class is to be instantiated once per source file to be compiled. It
 * contains the meta-data used for compiling (source file name, compilation
 * options) and the necessary utilities for compilation (symbol tables, abstract
 * representation of target file, ...).
 * <p>
 * It contains several objects specialized for different tasks. Delegate methods
 * are used to simplify the code of the caller (e.g. call
 * compiler.addInstruction() instead of compiler.getProgram().addInstruction()).
 *
 * @author gl29
 * @date 01/01/2024
 */
public class DecacCompiler {
    private static final Logger LOG = Logger.getLogger(DecacCompiler.class);

    /**
     * Portable newline character.
     */
    private static final String nl = System.getProperty("line.separator", "\n");

    private static boolean color = false;

    public DecacCompiler(CompilerOptions compilerOptions, File source) {
        super();
        this.compilerOptions = compilerOptions;
        this.source = source;
        if (compilerOptions != null) {
            color = compilerOptions.color;
        }
    }

    /**
     * Source file associated with this compiler instance.
     */
    public File getSource() {
        return source;
    }

    /**
     * Compilation options (e.g. when to stop compilation, number of registers
     * to use, ...).
     */
    public CompilerOptions getCompilerOptions() {
        return compilerOptions;
    }

    /**
     * @see fr.ensimag.ima.pseudocode.IMAProgram#add(fr.ensimag.ima.pseudocode.AbstractLine)
     */
    public void add(AbstractLine line) {
        program.add(line);
    }

    /**
     * @see fr.ensimag.ima.pseudocode.IMAProgram#addComment(java.lang.String)
     */
    public void addComment(String comment) {
        program.addComment(comment);
    }

    /**
     * @see fr.ensimag.ima.pseudocode.IMAProgram#addLabel(fr.ensimag.ima.pseudocode.Label)
     */
    public void addLabel(Label label) {
        program.addLabel(label);
    }

    /**
     * @see fr.ensimag.ima.pseudocode.IMAProgram#addInstruction(fr.ensimag.ima.pseudocode.Instruction)
     */
    public void addInstruction(Instruction instruction) {
        program.addInstruction(instruction);
    }

    /**
     * @see fr.ensimag.ima.pseudocode.IMAProgram#addInstruction(fr.ensimag.ima.pseudocode.Instruction,
     * java.lang.String)
     */
    public void addInstruction(Instruction instruction, String comment) {
        program.addInstruction(instruction, comment);
    }

    /**
     * @see fr.ensimag.ima.pseudocode.IMAProgram#display()
     */
    public String displayIMAProgram() {
        return program.display();
    }

    private final CompilerOptions compilerOptions;
    private final File source;
    /**
     * The main program. Every instruction generated will eventually end up here.
     */
    private final IMAProgram program = new IMAProgram();


    /**
     * The global environment for types, the symbolTable and the labelTable
     */
    public final EnvironmentType environmentType = new EnvironmentType(this);
    public SymbolTable symbolTable = new SymbolTable();

    public LabelTable labelTable = new LabelTable();

    public Symbol createSymbol(String name) {
        if (symbolTable == null) { // TODO : à enlever
            symbolTable = new SymbolTable();
        }
        return this.symbolTable.create(name);
    }
    // TODO : à enlever
    private int ifCounter = 0;

    public int getIfCounter() {
        return ifCounter;
    }

    public void incrementIfCounter() {
        ifCounter++;
    }

    public static boolean getColor() {
        return color;
    }

    /**
     * Run the compiler (parse source file, generate code)
     *
     * @return true on error
     */
    public boolean compile() {
        String sourceFile = source.getAbsolutePath();
        String destFile = sourceFile.replace(".deca", ".ass");

        PrintStream err = System.err;
        PrintStream out = System.out;
        LOG.debug("Compiling file " + sourceFile + " to assembly file " + destFile);


        try {
            return doCompile(sourceFile, destFile, out, err);
        } catch (LocationException e) {
            e.display(err);
            return true;
        } catch (DecacFatalError e) {
            err.println(e.getMessage());
            return true;
        } catch (StackOverflowError e) {
            LOG.debug("stack overflow", e);
            err.println("Stack overflow while compiling file " + sourceFile + ".");
            return true;
        } catch (Exception e) {
            LOG.fatal("Exception raised while compiling file " + sourceFile
                    + ":", e);
            err.println("Internal compiler error while compiling file " + sourceFile + ", sorry.");
            return true;
        } catch (AssertionError e) {
            LOG.fatal("Assertion failed while compiling file " + sourceFile
                    + ":", e);
            err.println("Internal compiler error while compiling file " + sourceFile + ", sorry.");
            return true;
        }
    }

    /**
     * Internal function that does the job of compiling (i.e. calling lexer,
     * verification and code generation).
     *
     * @param sourceName name of the source (deca) file
     * @param destName   name of the destination (assembly) file
     * @param out        stream to use for standard output (output of decac -p)
     * @param err        stream to use to display compilation errors
     * @return true on error
     */
    private boolean doCompile(String sourceName, String destName, PrintStream out, PrintStream err)
            throws DecacFatalError, LocationException {


        LOG.debug("compilation started");


        AbstractProgram prog = doLexingAndParsing(sourceName, err);
        if (prog == null) {
            LOG.info("Parsing failed");
            return true;
        }

        if (compilerOptions.maxStep == CompilerOptions.Step.PARSE) {
            // Display parse tree
            prog.decompile(out);
            return false;
        }

        assert (prog.checkAllLocations());
        prog.verifyProgram(this);
        assert (prog.checkAllDecorations());

        if (compilerOptions.maxStep == CompilerOptions.Step.VERIF) {
            return false;
        }

        addComment("start main program");
        prog.codeGenProgram(this);
        addComment("end main program");
        LOG.debug("Generated assembly code:" + nl + program.display());
        LOG.info("Output file assembly file is: " + destName);


        FileOutputStream fstream;
        try {
            fstream = new FileOutputStream(destName);
        } catch (FileNotFoundException e) {
            throw new DecacFatalError("Failed to open output file: " + e.getLocalizedMessage());
        }

        LOG.info("Writing assembler file ...");

        program.display(new PrintStream(fstream));
        LOG.info("Compilation of " + sourceName + " successful.");
        return false;
    }

    /**
     * Build and call the lexer and parser to build the primitive abstract
     * syntax tree.
     *
     * @param sourceName Name of the file to parse
     * @param err        Stream to send error messages to
     * @return the abstract syntax tree
     * @throws DecacFatalError    When an error prevented opening the source file
     * @throws DecacInternalError When an inconsistency was detected in the
     *                            compiler.
     * @throws LocationException  When a compilation error (incorrect program)
     *                            occurs.
     */
    protected AbstractProgram doLexingAndParsing(String sourceName, PrintStream err)
            throws DecacFatalError, DecacInternalError {

        LOG.debug("Lexing and parsing source file " + sourceName);
        LOG.debug("Building lexer ...");
        DecaLexer lex;
        try {
            lex = new DecaLexer(CharStreams.fromFileName(sourceName));
        } catch (IOException ex) {
            throw new DecacFatalError("Failed to open input file: " + ex.getLocalizedMessage());
        }
        lex.setDecacCompiler(this);
        CommonTokenStream tokens = new CommonTokenStream(lex);

        LOG.trace("Building token stream ...");


        LOG.debug("Building parser ...");

        DecaParser parser = new DecaParser(tokens);
        parser.setDecacCompiler(this);
        return parser.parseProgramAndManageErrors(err);
    }

    public GPRegister getRegister(int i) {
        return GPRegister.getR(i);
    }

    private int indexPile = 0;
    public DAddr nextRegisterOffset() {
        indexPile ++;
        return new RegisterOffset(indexPile, Register.GB);
    }

    private int registreLibre = 2;
    public GPRegister getNextRegistreLibre(){
        if(registreLibre == 15){
            throw new UnsupportedOperationException("Plus de registre libre");
        }
        return Register.getR(registreLibre ++);
    }
    public void libererRegistre(){
        registreLibre --;
    }

    public void libererRegistre(int i){
        registreLibre -= i;
    }

    public GPRegister getRegistreLibre() {
        return Register.getR(registreLibre );
    }
}
