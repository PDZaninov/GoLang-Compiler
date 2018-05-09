package com.oracle.app.nodes.expression;

import com.oracle.app.nodes.GoExpressionNode;
import com.oracle.truffle.api.frame.VirtualFrame;

public class GoKeyValueNode extends GoExpressionNode {

	private GoExpressionNode key;
	private GoExpressionNode value;
	private Object result;
	private Object keyResult;

	public GoKeyValueNode(GoExpressionNode key, GoExpressionNode value) {
		this.key = key;
		this.value = value;
		result = null;
		keyResult = null;
	}

	@Override
	public Object executeGeneric(VirtualFrame frame) {
		result = value.executeGeneric(frame);
		keyResult = key.executeGeneric(frame);
		return this;
	}

	public GoExpressionNode getKey() {
		return key;
	}

	public GoExpressionNode getValue() {
		return value;
	}
	
	public Object getResult() {
		return result;
	}

	public Object getKeyResult() { return keyResult; }

}
