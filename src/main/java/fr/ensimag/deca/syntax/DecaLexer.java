package fr.ensimag.deca.syntax;

import fr.ensimag.deca.DecacCompiler;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.atn.ATN;

public class DecaLexer extends AbstractDecaLexer{


    /**
     * Default constructor for the lexer.
     *
     * @param input
     */
    public DecaLexer(CharStream input) {
        super(input);
    }

    @Override
    public String[] getRuleNames() {
        return new String[0];
    }

    @Override
    public String getGrammarFileName() {
        return null;
    }

    @Override
    public ATN getATN() {
        return null;
    }
}
