package com.oracle.app.builtins;

import com.oracle.app.GoLanguage;
import com.oracle.app.nodes.GoExpressionNode;
import com.oracle.app.runtime.GoContext;
import com.oracle.truffle.api.dsl.GenerateNodeFactory;
import com.oracle.truffle.api.dsl.NodeChild;

@NodeChild(value = "arguments", type = GoExpressionNode[].class)
@GenerateNodeFactory
public abstract class GoBuiltinNode extends GoExpressionNode {

    /**
     * Accessor for the {@link GoContext}. The implementation of this method is generated
     * automatically based on the {@link NodeField} annotation on the class.
     */
    public final GoContext getContext() {
        return getRootNode().getLanguage(GoLanguage.class).getContextReference().get();
    }
}