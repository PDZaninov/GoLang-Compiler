package com.oracle.app.nodes.call;

import com.oracle.truffle.api.CallTarget;
import com.oracle.truffle.api.nodes.Node;

/*
 * Creates different dispatch nodes but there is only one dispatch node for now
 */
final public class GoUninitializedDispatchNode extends GoDispatchNode {

	@Override
	public Object executeDispatch(CallTarget target, Object[] arguments) {
		Node thisNode = this;
		GoDispatchNode replacementNode = new GoGenericDispatchNode();
		GoInvokeNode parentNode = (GoInvokeNode) thisNode.getParent();
		parentNode.dispatchNode.replace(replacementNode);
		return replacementNode.executeDispatch(target, arguments);
	}

}
