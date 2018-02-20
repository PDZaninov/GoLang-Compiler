package com.oracle.app.nodes.expression;

import com.oracle.truffle.api.dsl.NodeChild;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.nodes.NodeInfo;
import com.oracle.app.nodes.GoExpressionNode;
import com.oracle.app.nodes.GoUnaryNode;

/**
 * Example of a simple unary node that uses type specialization. See {@link SLAddNode} for
 * information on specializations.
 */
@NodeChild("valueNode")
@NodeInfo(shortName = "!")
public abstract class GoLogicalNotNode extends GoUnaryNode {

    @Specialization
    protected boolean doBoolean(boolean value) {
        return !value;
    }
}
