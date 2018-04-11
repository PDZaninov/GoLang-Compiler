package com.oracle.app.nodes.expression;

import com.oracle.app.nodes.GoUnaryNode;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.nodes.NodeInfo;

//shares same shortname as add node
@NodeInfo(shortName = "+")
public abstract class GoPositiveSignNode extends GoUnaryNode{
    @Specialization
    protected long positiveSign(long value) {
        return 0 + value;
    }

    
    @Specialization
    protected int positiveSign(int value) {
        return 0 + value;
    }
    
    @Specialization
    protected float positiveSign(float value) {
        return 0 + value;
    }

    @Specialization
    protected double positiveSign(double value) {
        return 0 + value;
    }
}
