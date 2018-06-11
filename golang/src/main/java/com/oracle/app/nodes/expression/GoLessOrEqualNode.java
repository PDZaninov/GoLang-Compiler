package com.oracle.app.nodes.expression;

import com.oracle.app.nodes.GoBinaryNode;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.nodes.NodeInfo;

@NodeInfo(shortName = "<=")
public abstract class GoLessOrEqualNode extends GoBinaryNode {

    @Specialization
    protected boolean lessOrEqual(int left, int right) {
        return left <= right;
    }
    
    @Specialization
    protected boolean lessOrEqual(float left, float right) {
        return left <= right;
    }

    @Specialization
    protected boolean lessOrEqual(double left, double right) {
        return left <= right;
    }
    
    @Specialization
    protected boolean lessOrEqual(String left, String right) {
        return left.compareTo(right) <= 0;
    }
}