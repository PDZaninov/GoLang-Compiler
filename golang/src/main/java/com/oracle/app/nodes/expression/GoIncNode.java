package com.oracle.app.nodes.expression;

import com.oracle.app.nodes.GoIncDecStmtNode;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.nodes.NodeInfo;

@NodeInfo(shortName = "++")
public abstract class GoIncNode extends GoIncDecStmtNode {
	
    @Specialization
    protected long increment(long value) {
        return value++;
    }

    @Specialization
    protected int increment(int value) {
        return value++;
    }
    
    @Specialization
    protected float increment(float value) {
        return value++;
    }
}
