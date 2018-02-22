package com.oracle.app.nodes.expression;

import com.oracle.app.nodes.GoBinaryNode;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.nodes.NodeInfo;

//shares same shortname as bitwise complement node
@NodeInfo(shortName = "^")
public abstract class GoBitwiseXORNode extends GoBinaryNode{
    @Specialization
    protected long bitwiseXOR(long left, long right) {
        return left ^ right;
    }

    
    @Specialization
    protected int  bitwiseXOR(int left, int right) {
        return left ^  right;
    }
}
