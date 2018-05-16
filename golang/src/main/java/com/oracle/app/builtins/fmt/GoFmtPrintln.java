package com.oracle.app.builtins.fmt;

import com.oracle.app.nodes.GoExpressionNode;
import com.oracle.truffle.api.CompilerDirectives.TruffleBoundary;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.NodeInfo;
 
@NodeInfo(shortName = "Println")
public class GoFmtPrintln extends GoExpressionNode {

	@Override
	public Object executeGeneric(VirtualFrame frame) {
		if(frame.getArguments().length < 1){
			return null;
		}
		Object[] arguments = frame.getArguments();
		StringBuilder builder = new StringBuilder();
		for(int i = 0; i < arguments.length; i++){
			builder.append(arguments[i].toString() +" ");
		}
		if(builder.length() > 1){
			builder.deleteCharAt(builder.length()-1);
		}
		doPrint(builder);
		return null;
	}
	
	@TruffleBoundary
	private void doPrint(StringBuilder builder) {
		System.out.println(builder);
	}

	public static GoFmtPrintln getFmtPrintln(){
		return new GoFmtPrintln();
	}
}