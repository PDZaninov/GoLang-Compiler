package com.oracle.app.runtime;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.oracle.app.GoLanguage;
import com.oracle.app.nodes.GoRootNode;
import com.oracle.truffle.api.RootCallTarget;
import com.oracle.truffle.api.Truffle;

/**
 * Manages the mapping from function names to {@link GoFunction function objects}.
 */
public final class GoFunctionRegistry {

    private final GoLanguage language;
    private final Map<String, GoFunction> functions = new HashMap<>();

    public GoFunctionRegistry(GoLanguage language) {
        this.language = language;
    }

    /**
     * Returns the canonical {@link GoFunction} object for the given name. If it does not exist yet,
     * it is created.
     */
    public GoFunction lookup(String name, boolean createIfNotPresent) {
        GoFunction result = functions.get(name);
        if (result == null && createIfNotPresent) {
            result = new GoFunction(language, name);
            functions.put(name, result);
        }
        return result;
    }

    /**
     * Associates the {@link GoFunction} with the given name with the given implementation root
     * node. If the function did not exist before, it defines the function. If the function existed
     * before, it redefines the function and the old implementation is discarded.
     */
    public GoFunction register(String name, GoRootNode rootNode) {
        GoFunction function = lookup(name, true);
        RootCallTarget callTarget = Truffle.getRuntime().createCallTarget(rootNode);
        function.setCallTarget(callTarget);
        return function;
    }

    public void register(Map<String, GoRootNode> newFunctions) {
        for (Map.Entry<String, GoRootNode> entry : newFunctions.entrySet()) {
            register(entry.getKey(), entry.getValue());
        }
    }
    
/*Used to redefine builtin functions
    public void register(Source newFunctions) {
        register(Parser.parseGo(language, newFunctions));
    }
*/
    /**
     * Returns the sorted list of all functions, for printing purposes only.
     */
    public List<GoFunction> getFunctions() {
        List<GoFunction> result = new ArrayList<>(functions.values());
        Collections.sort(result, new Comparator<GoFunction>() {
            public int compare(GoFunction f1, GoFunction f2) {
                return f1.toString().compareTo(f2.toString());
            }
        });
        return result;
    }

}