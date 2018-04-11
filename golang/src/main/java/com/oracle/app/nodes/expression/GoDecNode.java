package com.oracle.app.nodes.expression;

import com.oracle.app.nodes.GoIncDecStmtNode;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.nodes.NodeInfo;

@NodeInfo(shortName = "--")
public abstract class GoDecNode extends GoIncDecStmtNode {
	
    @Specialization
    protected long decrement(long value) {
        return value--;
    }

    @Specialization
    protected int decrement(int value) {
        return value--;
    }
    
    @Specialization
    protected float decrement(float value) {
        return value--;
    }

    @Specialization
    protected double decrement(double value) {
        return value--;
    }
}