package com.oracle.app.nodes.local;

import com.oracle.app.nodes.GoExpressionNode;
import com.oracle.app.runtime.GoNull;
import com.oracle.truffle.api.frame.VirtualFrame;

public class GoReadArgumentsNode extends GoExpressionNode{

	private final int index;
	
	//variable thing that does branch profiling
	
	public GoReadArgumentsNode(int index){
		this.index = index;
	}

	@Override
	public Object executeGeneric(VirtualFrame frame) {
		Object[] args = frame.getArguments();
		if(index < args.length){
			return args[index];
		}
		else{
			return GoNull.SINGLETON;
		}
	}
	
}
