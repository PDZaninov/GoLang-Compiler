package com.oracle.app.nodes;

import com.oracle.truffle.api.dsl.NodeChild;
import com.oracle.truffle.api.dsl.NodeChildren;


@NodeChild("valueNode")
public abstract class GoUnaryNode extends GoExpressionNode{

}
