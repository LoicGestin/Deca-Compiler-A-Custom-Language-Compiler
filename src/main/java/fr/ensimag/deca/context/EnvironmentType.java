package fr.ensimag.deca.context;

import fr.ensimag.deca.DecacCompiler;
import java.util.HashMap;
import java.util.Map;
import fr.ensimag.deca.tools.SymbolTable.Symbol;
import fr.ensimag.deca.tree.Location;

// A FAIRE: étendre cette classe pour traiter la partie "avec objet" de Déca
// [DONE]
/**
 * Environment containing types. Initially contains predefined identifiers, more
 * classes can be added with declareClass().
 *
 * @author gl29
 * @date 01/01/2024
 */
public class EnvironmentType {
    public EnvironmentType(DecacCompiler compiler) {
        
        envTypes = new HashMap<>();
        
        Symbol intSymb = compiler.createSymbol("int");
        INT = new IntType(intSymb);
        envTypes.put(intSymb, new TypeDefinition(INT, Location.BUILTIN));

        Symbol floatSymb = compiler.createSymbol("float");
        FLOAT = new FloatType(floatSymb);
        envTypes.put(floatSymb, new TypeDefinition(FLOAT, Location.BUILTIN));

        Symbol voidSymb = compiler.createSymbol("void");
        VOID = new VoidType(voidSymb);
        envTypes.put(voidSymb, new TypeDefinition(VOID, Location.BUILTIN));

        Symbol booleanSymb = compiler.createSymbol("boolean");
        BOOLEAN = new BooleanType(booleanSymb);
        envTypes.put(booleanSymb, new TypeDefinition(BOOLEAN, Location.BUILTIN));

        Symbol objectSymb = compiler.createSymbol("Object");
        OBJECT = new ClassType(objectSymb, new Location(0, 0, "Object"), null);
        envTypes.put(objectSymb, new TypeDefinition(OBJECT, Location.BUILTIN));

    }

    private final Map<Symbol, TypeDefinition> envTypes;

    public Map<Symbol, TypeDefinition> getEnvTypes() {
        return envTypes;
    }

    public boolean compatible(Type t1, Type t2){
        return t1.isFloat() && t2.isInt() || t1.isInt() && t2.isFloat() || t1.sameType(t2);
    }

    public TypeDefinition defOfType(Symbol s) {
        return envTypes.get(s);
    }


    public void declareClass(Symbol key, ClassDefinition value) throws EnvironmentExp.DoubleDefException {
        if (envTypes.containsKey(key)) {
            throw new EnvironmentExp.DoubleDefException();
        }
        envTypes.put(key, new TypeDefinition(value.getType(), value.getLocation()));
    }


    public final VoidType    VOID;
    public final IntType     INT;
    public final FloatType   FLOAT;
    public final BooleanType BOOLEAN;
    public final ClassType   OBJECT;
}
