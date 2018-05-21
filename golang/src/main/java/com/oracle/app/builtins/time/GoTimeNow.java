package com.oracle.app.builtins.time;

import com.oracle.app.nodes.GoExpressionNode;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.NodeInfo;

@NodeInfo(shortName = "Now")
public class GoTimeNow extends GoExpressionNode{

	public static GoExpressionNode getTimeNow() {
		// TODO Auto-generated method stub
		return new GoTimeNow();
	}

	@Override
	public Object executeGeneric(VirtualFrame frame) {
		return System.nanoTime();
	}

}
