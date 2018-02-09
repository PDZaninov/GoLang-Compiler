package com.oracle.app.nodes.call;

import com.oracle.app.nodes.GoTypes;
import com.oracle.app.runtime.GoFunction;
import com.oracle.truffle.api.Truffle;
import com.oracle.truffle.api.dsl.TypeSystemReference;
import com.oracle.truffle.api.nodes.IndirectCallNode;
import com.oracle.truffle.api.nodes.Node;

@TypeSystemReference(GoTypes.class)
public class GoGenericDispatchNode extends Node {

	@Child private IndirectCallNode callNode = Truffle.getRuntime().createIndirectCallNode();
	
	public Object executeDispatch(GoFunction function, Object[] arguments) {
		return callNode.call(function.getCallTarget(), arguments);
	}

}
