package fr.ensimag.deca.context;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.tools.SymbolTable.Symbol;
import fr.ensimag.deca.tree.Location;

import java.util.HashMap;
import java.util.Map;

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
    public final VoidType VOID;
    public final IntType INT;
    public final FloatType FLOAT;
    public final BooleanType BOOLEAN;
    public final NullType NULL;
    public final ClassType OBJECT;
    private final Map<Symbol, TypeDefinition> envTypes;
    private final Map<Symbol, ClassDefinition> envClasses;


    public EnvironmentType(DecacCompiler compiler) {
        envTypes = new HashMap<>();
        envClasses = new HashMap<>();

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

        Symbol nullSymb = compiler.createSymbol("null");
        NULL = new NullType(nullSymb);
        envTypes.put(nullSymb, new TypeDefinition(NULL, Location.BUILTIN));

        Symbol objectSymb = compiler.createSymbol("Object");
        OBJECT = new ClassType(objectSymb, new Location(0, 0, "Object"), null);
        envTypes.put(objectSymb, new TypeDefinition(OBJECT, Location.BUILTIN));
        envClasses.put(objectSymb, new ClassDefinition(OBJECT, Location.BUILTIN, null));

    }

    public boolean subType(EnvironmentType envTypes, Type t1, Type t2) {
        System.out.println("Je suis entré dans subType");
        if(t1.sameType(t2) && !t1.isClassOrNull() && !t2.isClassOrNull()) {
            return true;
        }

    	if (t1.isClass() && t2.isClassOrNull()) {
            System.out.println("Je suis entré dans subType 2");
            if(t1.isClass() && t2.isNull()) {
                return true;
            }

            if(t1.isClass() && !t2.isClass()){
                return false;
            }

    		ClassDefinition def1 = envTypes.getClassDefinition(t1.getName());
    		ClassDefinition def2 = envTypes.getClassDefinition(t2.getName());
            System.out.println("type def1 : "+def1.getType() + " type def2 : "+def2.getType());
            System.out.println("Salut " + def2.getType().isSubClassOf(def1.getType()));
            return def2.getType().isSubClassOf(def1.getType());
    	}
        System.out.println("Je suis sorti de subType");
    	return false;
    }

    public boolean compatible(Type t1, Type t2) {
        System.out.println("Je suis entré dans compatible");
        return t1.isFloat() && t2.isInt() || subType(this, t1, t2);
    }

    public TypeDefinition defOfType(Symbol s) {
        return envTypes.get(s);
    }

    public ClassDefinition defOfClass(Symbol s) {
    	return envClasses.get(s);
    }

    public void declareClass(Symbol key, ClassDefinition value, ClassDefinition superClass) throws EnvironmentExp.DoubleDefException {
        if (envTypes.containsKey(key)) {
            throw new EnvironmentExp.DoubleDefException();
        }

        if(envClasses.containsKey(key)) {
            throw new EnvironmentExp.DoubleDefException();
        }

        envTypes.put(key, new TypeDefinition(value.getType(), value.getLocation()));
        envClasses.put(key, new ClassDefinition(value.getType(), value.getLocation(), superClass));
    }

    public ClassDefinition getClassDefinition(Symbol key) {
    	return envClasses.get(key);
    }

    public void afficher() {
    	System.out.println("Affichage de l'environnement : ");
    	for (Symbol key : envTypes.keySet()) {
    		System.out.println(key + " : " + envTypes.get(key));
    	}
    	System.out.println("Fin de l'affichage de l'environnement.");
    }
}
