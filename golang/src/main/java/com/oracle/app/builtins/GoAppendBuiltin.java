package com.oracle.app.builtins;

import com.oracle.app.nodes.GoExpressionNode;
import com.oracle.app.nodes.types.GoSlice;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.NodeInfo;

@NodeInfo(shortName = "append")
public class GoAppendBuiltin extends GoExpressionNode {

	@Override
	public Object executeGeneric(VirtualFrame frame) {
		if(frame.getArguments().length < 1){
			return null;
		}
		Object[] arguments = frame.getArguments();
		GoSlice slice = (GoSlice) arguments[0];
		for(int i = 1; i < arguments.length; i++){
			
		}
		return null;
	}
	
	public static GoAppendBuiltin getAppendBuiltin(){
		return new GoAppendBuiltin();
	}

}
