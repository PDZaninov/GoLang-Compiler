package com.oracle.app.nodes.expression;

import com.oracle.app.nodes.GoUnaryNode;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.nodes.NodeInfo;

//shares same shortname as bitwise XOR node
@NodeInfo(shortName = "^")
public abstract class GoBitwiseComplementNode extends GoUnaryNode{

    @Specialization
    protected int bitwiseComplement(int value) {
        return ~value;
    }
    

}
