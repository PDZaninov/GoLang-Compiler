package com.oracle.app.nodes.expression;

import com.oracle.truffle.api.CallTarget;
import com.oracle.truffle.api.CompilerDirectives;
import com.oracle.truffle.api.CompilerDirectives.CompilationFinal;
import com.oracle.truffle.api.TruffleLanguage.ContextReference;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.NodeInfo;
import com.oracle.app.GoLanguage;
import com.oracle.app.nodes.GoExpressionNode;
import com.oracle.app.runtime.GoContext;
import com.oracle.app.runtime.GoFunction;
import com.oracle.app.runtime.GoFunctionRegistry;

/**
 * Constant literal for a {@link SLFunction function} value, created when a function name occurs as
 * a literal in SL source code. Note that function redefinition can change the {@link CallTarget
 * call target} that is executed when calling the function, but the {@link SLFunction} for a name
 * never changes. This is guaranteed by the {@link SLFunctionRegistry}.
 */
@NodeInfo(shortName = "func")
public final class GoFunctionLiteralNode extends GoExpressionNode {

    /** The name of the function. */
    private final String functionName;

    /**
     * The resolved function. During parsing (in the constructor of this node), we do not have the
     * {@link SLContext} available yet, so the lookup can only be done at {@link #executeGeneric
     * first execution}. The {@link CompilationFinal} annotation ensures that the function can still
     * be constant folded during compilation.
     */
    @CompilationFinal
    private GoFunction cachedFunction;

    private final ContextReference<GoContext> reference;

    public GoFunctionLiteralNode(GoLanguage language, String functionName) {
        this.functionName = functionName;
        this.reference = language.getContextReference();
    }

    @Override
    public GoFunction executeGeneric(VirtualFrame frame) {
        if (cachedFunction == null) {
            CompilerDirectives.transferToInterpreterAndInvalidate();
            /* We are about to change a @CompilationFinal field. */
            CompilerDirectives.transferToInterpreterAndInvalidate();
            /* First execution of the node: lookup the function in the function registry. */
            cachedFunction = reference.get().getFunctionRegistry().lookup(functionName, true);
        }
        return cachedFunction;
    }

}
