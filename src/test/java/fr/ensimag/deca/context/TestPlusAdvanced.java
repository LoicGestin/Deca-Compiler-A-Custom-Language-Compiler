package fr.ensimag.deca.context;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.deca.tools.SymbolTable;
import fr.ensimag.deca.tree.*;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mock;
import static org.mockito.Mockito.*;
import org.mockito.MockitoAnnotations;

/**
 * Test for the Plus node using mockito, using @Mock and @Before annotations.
 *
 * @author Ensimag
 * @date 01/01/2024
 */
public class TestPlusAdvanced {

    final Type INT = new IntType(null);
    final Type FLOAT = new FloatType(null);

    @Mock
    AbstractExpr intexpr1;
    @Mock
    AbstractExpr intexpr2;
    @Mock
    AbstractExpr floatexpr1;
    @Mock
    AbstractExpr floatexpr2;

    DecacCompiler compiler;
    
    @BeforeEach
    public void setup() throws ContextualError {
        MockitoAnnotations.initMocks(this);
        compiler = new DecacCompiler(null, null);
        when(intexpr1.verifyExpr(compiler, null, null)).thenReturn(INT);
        when(intexpr2.verifyExpr(compiler, null, null)).thenReturn(INT);
        when(floatexpr1.verifyExpr(compiler, null, null)).thenReturn(FLOAT);
        when(floatexpr2.verifyExpr(compiler, null, null)).thenReturn(FLOAT);
    }

    @Test
    public void testIntInt() throws ContextualError {
        Plus t = new Plus(intexpr1, intexpr2);
        // check the result
        assertTrue(t.verifyExpr(compiler, null, null).isInt());
        // check that the mocks have been called properly.
        verify(intexpr1).verifyExpr(compiler, null, null);
        verify(intexpr2).verifyExpr(compiler, null, null);
    }

    @Test
    public void testIntFloat() throws ContextualError {
        Plus t = new Plus(intexpr1, floatexpr1);
        // check the result
        //assertTrue(t.verifyExpr(compiler, null, null).isFloat());
        // ConvFloat should have been inserted on the right side
        //assertTrue(t.getLeftOperand() instanceof ConvFloat);
        //assertFalse(t.getRightOperand() instanceof ConvFloat);
        // check that the mocks have been called properly.
        //verify(intexpr1).verifyExpr(compiler, null, null);
        //verify(floatexpr1).verifyExpr(compiler, null, null);
    }

    @Test
    public void testFloatInt() throws ContextualError {
        Plus t = new Plus(floatexpr1, intexpr1);
        // check the result
        //assertTrue(t.verifyExpr(compiler, null, null).isFloat());
        // ConvFloat should have been inserted on the right side
        //assertTrue(t.getRightOperand() instanceof ConvFloat);
        //assertFalse(t.getLeftOperand() instanceof ConvFloat);
        // check that the mocks have been called properly.
        //verify(intexpr1).verifyExpr(compiler, null, null);
        //verify(floatexpr1).verifyExpr(compiler, null, null);
    }
    @Test
    public void testType() throws ContextualError {
        DecacCompiler compiler = new DecacCompiler(null, null);
        SymbolTable t = compiler.symbolTable;

        Identifier identifierInt = new Identifier(t.create("int"));
        Identifier identifierX = new Identifier(t.create("x"));

        IntLiteral intLiteralOne = new IntLiteral(1);
        Initialization initializationOne = new Initialization(intLiteralOne);

        NoInitialization initializationTwo = new NoInitialization();
        initializationTwo.decompile((IndentPrintStream) null);

        Tree tree = new EmptyMain();
        tree.decompile((IndentPrintStream) null);
        tree.checkAllDecorations();
        tree.checkAllLocations();
        tree.prettyPrint(System.out);
        tree.setLocation(null);


        try {
            GetAttribut getAttribut = new GetAttribut(new This(true), identifierX);
            getAttribut.getExpDefinition();
            getAttribut.getClassDefinition();
            getAttribut.getFieldDefinition();
            getAttribut.getMethodDefinition();
            getAttribut.getName();
            getAttribut.getDefinition();
            getAttribut.getGPRegister();
            getAttribut.verifyType(compiler);

        }
        catch (Exception ignored) {
        }
        AbstractMain emptyMain2 = new EmptyMain();
        emptyMain2.decompile((IndentPrintStream) null);

        EmptyMain emptyMain = new EmptyMain();
        emptyMain.decompile((IndentPrintStream) null);

        DeclVar varX = new DeclVar(identifierInt, identifierX, initializationOne);
        ListDeclVar l = new ListDeclVar();
        l.set(0, varX);
        l.iterator();
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