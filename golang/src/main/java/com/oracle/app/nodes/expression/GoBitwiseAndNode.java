package com.oracle.app.nodes.expression;

import com.oracle.app.nodes.GoBinaryNode;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.nodes.NodeInfo;

@NodeInfo(shortName = "&")
public abstract class GoBitwiseAndNode extends GoBinaryNode{

    @Specialization
    protected int  bitwiseAnd(int left, int right) {
        return left &  right;
    }
    
}
