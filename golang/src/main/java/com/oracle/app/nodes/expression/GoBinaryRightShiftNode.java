package com.oracle.app.nodes.expression;

import com.oracle.app.nodes.GoBinaryNode;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.nodes.NodeInfo;

//TODO Check if ints are the only thing that can be binary shifted
@NodeInfo(shortName = ">>")
public abstract class GoBinaryRightShiftNode extends GoBinaryNode{
	
    @Specialization
    protected int  rightShift(int left, int right) {
        return left >> right;
    }
}
