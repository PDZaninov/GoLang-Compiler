package com.oracle.app.nodes.expression;

import com.oracle.app.nodes.GoUnaryNode;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.nodes.NodeInfo;

/**
 * Example of a simple unary node that uses type specialization. See {@link SLAddNode} for
 * information on specializations.
 */
@NodeInfo(shortName = "!")
public abstract class GoLogicalNotNode extends GoUnaryNode {

    @Specialization
    protected boolean doBoolean(boolean value) {
        return !value;
    }
}
