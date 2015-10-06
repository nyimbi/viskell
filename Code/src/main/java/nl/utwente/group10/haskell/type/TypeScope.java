package nl.utwente.group10.haskell.type;

import java.util.HashMap;
import java.util.HashSet;
import java.util.IdentityHashMap;

public class TypeScope {
    /**
     * Offset for the creation of unique type variable names.
     */
    static int tvOffset = 0;
    
    /**
     * Lookup table by textual name for type variable within this scope 
     */
    private HashMap<String, TypeVar> vars;
    
    /**
     * The mapping between known type instances and their related fresh type variables
     */
    private IdentityHashMap<TypeVar.TypeInstance, TypeVar> staleToFresh;
    
    public TypeScope() {
        this.vars = new HashMap<>();
        this.staleToFresh = new IdentityHashMap<>();
    }
    
    /**
     * Looks up a type variable by name in this scope or constructs a new one if it doesn't exists yet.
     * @param name The textual representation of the type variable.
     * @return TypeVar that is (now) associated with the name
     */
    public TypeVar getVar(String name) {
        if (this.vars.containsKey(name)) {
            return this.vars.get(name);
        }
        
        TypeVar var = new TypeVar(name, new HashSet<>(), null);
        this.vars.put(name, var);
        return var;
    }

    /**
     * Helper method that add an extra constraint to an type variable, for use in parsing types.
     * @param name The textual representation of the type variable.
     * @param typeClass The constraint to be added to the type variable.
     */
    public void introduceConstraint(String name, TypeClass typeClass) {
        TypeVar var = this.getVar(name);
        var.introduceConstraint(typeClass);
    }
    
    /**
     * This internal method should only be called from the TypeVar in the argument
     */
    protected TypeVar pickFreshTypeVar(TypeVar var) {
        return var.pickFreshTypeVarInstance(this.staleToFresh);
    }

    /**
     * Produces a new type variable with a unique name.
     * @param prefix A string to prepend to the unique name.
     * @return The new type variable
     */
    public static TypeVar unique(String prefix) {
        return new TypeVar(prefix + "___" + Integer.toHexString(tvOffset++), new HashSet<>(), null);
    }

    
}
