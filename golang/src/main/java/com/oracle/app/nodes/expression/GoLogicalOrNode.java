package com.oracle.app.nodes.expression;

import com.oracle.app.nodes.GoExpressionNode;
import com.oracle.truffle.api.nodes.NodeInfo;

/**
 * Logical disjunction node with short circuit evaluation.
 */
@NodeInfo(shortName = "||")
public final class GoLogicalOrNode extends GoShortCircuitNode {

    public GoLogicalOrNode(GoExpressionNode left, GoExpressionNode right) {
        super(left, right);
    }

    @Override
    protected boolean isEvaluateRight(boolean left) {
        return !left;
    }

    @Override
    protected boolean execute(boolean left, boolean right) {
        return left || right;
    }

}