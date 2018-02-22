package com.oracle.app.nodes.expression;

import com.oracle.app.nodes.GoUnaryNode;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.nodes.NodeInfo;

@NodeInfo(shortName = "^")
public abstract class GoBitwiseComplementNode extends GoUnaryNode{
    @Specialization
    protected long bitwiseComplement(long value) {
        return ~value;
    }

    
    @Specialization
    protected int bitwiseComplement(int value) {
        return ~value;
    }
    

}
