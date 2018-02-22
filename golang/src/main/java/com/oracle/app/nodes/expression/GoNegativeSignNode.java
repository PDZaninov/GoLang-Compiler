package com.oracle.app.nodes.expression;

import com.oracle.app.nodes.GoUnaryNode;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.nodes.NodeInfo;

//shares same shortname as sub node
@NodeInfo(shortName = "-")
public abstract class GoNegativeSignNode extends GoUnaryNode{
	
    @Specialization
    protected long negativeSign(long value) {
        return 0 - value;
    }

    
    @Specialization
    protected int negativeSign(int value) {
        return 0 - value;
    }
    
    @Specialization
    protected float negativeSign(float value) {
        return 0 - value;
    }
}
