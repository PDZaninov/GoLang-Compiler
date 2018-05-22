package com.oracle.app.builtins.time;

import java.time.Instant;

import com.oracle.app.GoLanguage;
import com.oracle.app.nodes.GoExpressionNode;
import com.oracle.app.nodes.GoRootNode;
import com.oracle.app.runtime.GoContext;
import com.oracle.app.runtime.GoFunction;
import com.oracle.truffle.api.RootCallTarget;
import com.oracle.truffle.api.Truffle;
import com.oracle.truffle.api.frame.FrameDescriptor;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.NodeInfo;

@NodeInfo(shortName = "Now")
public class GoTimeNow extends GoExpressionNode{

	private GoLanguage language;
	private GoFunction unixNano;
	
	public GoTimeNow(GoLanguage language){
		this.language = language;
	}
	
	public static GoExpressionNode getTimeNow(GoLanguage language) {
		// TODO Auto-generated method stub
		return new GoTimeNow(language);
	}

	@Override
	public Object executeGeneric(VirtualFrame frame) {
		GoExpressionNode body = GoUnixNano.getUnixNano();
		String name = GoContext.lookupNodeInfo(body.getClass()).shortName();
		GoRootNode rootNode = new GoRootNode(language, new FrameDescriptor(), null, null, body, null, name);
		GoFunction function = new GoFunction(language, name);
		RootCallTarget callTarget = Truffle.getRuntime().createCallTarget(rootNode);
        function.setCallTarget(callTarget);
        unixNano = function;
		return this;
	}
	
	public GoFunction getUnixNano(){
		return unixNano;
	}

}
