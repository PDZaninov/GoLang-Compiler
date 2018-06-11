package com.oracle.app.nodes.expression;

import com.oracle.app.nodes.GoBinaryNode;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.nodes.NodeInfo;


@NodeInfo(shortName = "<")
public abstract class GoLessThanNode extends GoBinaryNode {

    @Specialization
    protected boolean  lessThan(int left, int right) {
        return left < right;
    }
    
    @Specialization
    protected boolean  lessThan(float left, float right) {
        return left < right;
    }

    @Specialization
    protected boolean  lessThan(double left, double right) {
        return left < right;
    }
    
    @Specialization
    protected boolean  lessThan(String left, String right) {
        return left.compareTo(right) < 0;
    }
}