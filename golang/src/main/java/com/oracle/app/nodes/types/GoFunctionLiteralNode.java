package com.oracle.app.nodes.types;

import com.oracle.app.GoLanguage;
import com.oracle.app.nodes.GoExpressionNode;
import com.oracle.app.runtime.GoContext;
import com.oracle.truffle.api.TruffleLanguage.ContextReference;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.NodeInfo;

@NodeInfo(shortName = "func")
public final class GoFunctionLiteralNode extends GoExpressionNode {

    private final String functionName;

    private final ContextReference<GoContext> reference;

    public GoFunctionLiteralNode(GoLanguage language, String functionName) {
        this.functionName = functionName;
        this.reference = language.getContextReference();
    }

    @Override
    public Object executeGeneric(VirtualFrame frame){
        return reference.get().getFunctionRegistry().lookup(functionName, true);
    }
}