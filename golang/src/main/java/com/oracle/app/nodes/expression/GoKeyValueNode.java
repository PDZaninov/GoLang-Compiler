package com.oracle.app.nodes.expression;

import com.oracle.app.nodes.GoExpressionNode;
import com.oracle.truffle.api.frame.VirtualFrame;

public class GoKeyValueNode extends GoExpressionNode {

	private String key;
	private GoExpressionNode value;
	private Object result;

	public GoKeyValueNode(String key, GoExpressionNode value) {
		this.key = key;
		this.value = value;
		result = null;
	}

	@Override
	public Object executeGeneric(VirtualFrame frame) {
		result = value.executeGeneric(frame);
		return this;
	}

	public String getKey() {
		return key;
	}

	public GoExpressionNode getValue() {
		return value;
	}
	
	public Object getResult() {
		return result;
	}

}
