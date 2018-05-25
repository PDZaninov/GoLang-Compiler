package com.oracle.app.builtins.time;

import java.time.Instant;

import com.oracle.app.nodes.GoExpressionNode;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.NodeInfo;

@NodeInfo(shortName = "UnixNano")
public class GoUnixNano extends GoExpressionNode{

	public static GoExpressionNode getUnixNano() {
		// TODO Auto-generated method stub
		return new GoUnixNano();
	}

	@Override
	public Object executeGeneric(VirtualFrame frame) {
		//long result = Instant.now().toEpochMilli();
		return System.nanoTime();
		//return result;
	}
	
}
