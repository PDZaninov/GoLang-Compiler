package com.oracle.app.nodes;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.oracle.app.nodes.GoExpressionNode;
import com.oracle.app.nodes.GoStatementNode;
import com.oracle.app.nodes.types.GoStringNode;
import com.oracle.truffle.api.CompilerAsserts;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.ExplodeLoop;
import com.oracle.truffle.api.nodes.Node.Children;
import com.oracle.truffle.api.nodes.NodeInfo;

/**
 * Node holding a child node which holds the array of node arguments
 */
@NodeInfo(shortName = "Expr", description = "The node implementing a source code Expr")
public final class GoExprNode extends GoExpressionNode {


    @Child private GoExpressionNode bodyNode;

    public GoExprNode(GoExpressionNode bodyNode) {
        this.bodyNode = bodyNode;
    }

	@Override
	public Object executeGeneric(VirtualFrame frame) {
		
		return bodyNode.executeGeneric(frame);
	}
	
}