package com.oracle.app.nodes.expression;

import com.oracle.app.nodes.GoUnaryNode;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.nodes.NodeInfo;

@NodeInfo(shortName = "!")
public abstract class GoLogicalNotNode extends GoUnaryNode {

    @Specialization
    protected boolean doBoolean(boolean value) {
        return !value;
    }
}
