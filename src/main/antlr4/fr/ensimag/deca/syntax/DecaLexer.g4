lexer grammar DecaLexer;

options {
   language=Java;
   // Tell ANTLR to make the generated lexer class extend the
   // the named class, which is where any supporting code and
   // variables will be placed.
   superClass = AbstractDecaLexer;
}

@members {
}

// To debug => END : ('endif'|'end') {System.out.println("found an end");} ;
// Ref      => https://github.com/antlr/antlr4/blob/master/doc/lexer-rules.md


EOL : '\r'? '\n' ;

// =====================
// Reserved words

ASM : 'asm' ;
CLASS : 'class' ;
EXTENDS : 'extends' ;
ELSE : 'else' ;
FALSE : 'false' ;
IF : 'if' ;
INSTANCEOF : 'instanceof' ;
NEW : 'new' ;
NULL : 'null' ;
READINT : 'readInt' ;
READFLOAT : 'readFloat' ;
PRINT : 'print' ;
PRINTLN : 'println' ;
PRINTLNX : 'printlnx' ;
PRINTX : 'printx' ;
PROTECTED : 'protected' ;
RETURN : 'return' ;
THIS : 'this' ;
TRUE : 'true' ;
WHILE : 'while' ;

// =====================
// Identifiers (not reserved words)



fragment LETTER : [a-zA-Z] ;
fragment DIGIT : [0-9] ;
IDENT : (LETTER + '$' + '_')(LETTER + DIGIT + '$' + '_')* ;

// =====================
// Operators

LT : '<' ;
GT : '>' ;
EQ : '=' ;
PLUS : '+' ;
MINUS : '-' ;
TIMES : '*' ;
DIVIDE : '/' ;
MODULO : '%' ;
DOT : '.' ;
COMMA : ',' ;
LPAREN : '(' ;
RPAREN : ')' ;
LBRACE : '{' ;
RBRACE : '}' ;
EXCLAM : '!' ;
SEMI : ';' ;
EQEQ : '==' ;
NEQ : '!=' ;
GEQ : '>=' ;
LEQ : '<=' ;
AND : '&&' ;
OR : '||' ;

// =====================
// Integer literals

fragment POSITIVE_DIGIT : [1-9] ;
INT : '0' + POSITIVE_DIGIT DIGIT*;

// =====================
// Floating-point literals

fragment NUM : DIGIT+;
fragment EXP : ('E' + 'e') ('+' + '-')? NUM;
fragment DEC : NUM '.' NUM;
fragment FLOATDEC : (DEC + DEC EXP) ('F' + 'f')?;
fragment DIGITHEX : [0-9A-Fa-f];
fragment NUMHEX : DIGITHEX+;
fragment FLOATHEX : ('0x' + '0X') NUMHEX '.' NUMHEX ('P' + 'p') ('+' + '-')? NUM ('F' + 'f');
FLOAT : FLOATDEC + FLOATHEX;

// =====================
// Strings


fragment STRING_CAR : [^\\"\n\r] ; // Stringcar is all characters except " and \ and EOL
STRING : '"' (STRING_CAR + '\\"' + '\\\\')* '"' ;
MULTI_LINE_STRING : '"' (STRING_CAR + EOL + '\\"' + '\\\\')* '"' ;

// =====================
// Comments

MULTI_LINE_COMMENT : '/*' .*? '*/' -> skip ;
// Single line comment starts with // and ends with EOL or EOF
SINGLE_LINE_COMMENT : '//' .*? (EOL + EOF) -> skip ;

// =====================
// Whitespace

WS : [ \t\n\r]+ -> skip ;

// =====================
// File inclusion

fragment FILENAME : (LETTER + DIGIT + '.' + '-' + '_')+;
INCLUDE : '#include' (' ')* '"' FILENAME '"' ;