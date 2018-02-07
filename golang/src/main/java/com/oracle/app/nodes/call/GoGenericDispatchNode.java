package com.oracle.app.nodes.call;

import com.oracle.truffle.api.CallTarget;
import com.oracle.truffle.api.Truffle;
import com.oracle.truffle.api.nodes.IndirectCallNode;

public class GoGenericDispatchNode extends GoDispatchNode {

	@Child private IndirectCallNode callNode = Truffle.getRuntime().createIndirectCallNode();
	
	@Override
	public Object executeDispatch(CallTarget target, Object[] arguments) {
		return this.callNode.call(target, arguments);
	}

}
