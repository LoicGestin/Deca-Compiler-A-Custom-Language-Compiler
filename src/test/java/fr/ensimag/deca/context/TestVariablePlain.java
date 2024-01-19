package fr.ensimag.deca.context;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.tools.SymbolTable;
import fr.ensimag.deca.tree.*;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class TestVariablePlain {

    @Test
    public void testType() throws ContextualError {
        DecacCompiler compiler = new DecacCompiler(null, null);
        SymbolTable t = compiler.symbolTable;

        Identifier identifierInt = new Identifier(t.create("int"));
        Identifier identifierX = new Identifier( t.create("x"));

        IntLiteral intLiteralOne = new IntLiteral(1);
        Initialization initializationOne = new Initialization(intLiteralOne);


        DeclVar varX = new DeclVar(identifierInt, identifierX, initializationOne);
        ListDeclVar l = new ListDeclVar();
        l.add(varX);
        Main main = new Main(l, new ListInst());
        Program prog = new Program(new ListDeclClass(), main);
        prog.verifyProgram(compiler);
        DecacCompiler.setDebug(true);
        DecacCompiler.setColor(true);
        DecacCompiler.setNocheck(true);
        prog.codeGenProgram(compiler);
    }
}
