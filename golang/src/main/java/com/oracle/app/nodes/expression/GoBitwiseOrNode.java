package com.oracle.app.nodes.expression;

import com.oracle.app.nodes.GoBinaryNode;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.nodes.NodeInfo;

@NodeInfo(shortName = "|")
public abstract class GoBitwiseOrNode extends GoBinaryNode{
    @Specialization
    protected long bitwiseOr(long left, long right) {
        return left | right;
    }

    
    @Specialization
    protected int  bitwiseOr(int left, int right) {
        return left |  right;
    }
}
