package com.oracle.app.nodes.expression;

import com.oracle.app.nodes.GoBinaryNode;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.nodes.NodeInfo;

@NodeInfo(shortName = ">=")
public abstract class GoGreaterOrEqualNode extends GoBinaryNode {
    
    @Specialization
    protected boolean greaterOrEqual(int left, int right) {
        return left >= right;
    }
    
    @Specialization
    protected boolean greaterOrEqual(float left, float right) {
        return left >= right;
    }

    @Specialization
    protected boolean greaterOrEqual(double left, double right) {
        return left >= right;
    }
    
    @Specialization
    protected boolean greaterOrEqual(String left, String right) {
        return left.compareTo(right) >= 0;
    }
}
