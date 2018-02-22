package com.oracle.app.nodes.expression;

import com.oracle.app.nodes.GoBinaryNode;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.nodes.NodeInfo;

@NodeInfo(shortName = ">>")
public abstract class GoBinaryRightShiftNode extends GoBinaryNode{
	
    @Specialization
    protected long rightShift(long left, long right) {
        return left >> right;
    }

    
    @Specialization
    protected int  rightShift(int left, int right) {
        return left >> right;
    }
}
