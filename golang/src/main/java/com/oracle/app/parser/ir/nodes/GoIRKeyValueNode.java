package com.oracle.app.parser.ir.nodes;

import com.oracle.app.parser.ir.GoBaseIRNode;
import com.oracle.app.parser.ir.GoIRVisitor;

public class GoIRKeyValueNode extends GoBaseIRNode {
	
	GoBaseIRNode key;
	String colon;
	GoBaseIRNode value;

	public GoIRKeyValueNode(GoBaseIRNode key, String colon, GoBaseIRNode value) {
		super("Key Value Expression Node");
		this.key = key;
		this.colon = colon;
		this.value = value;
	}

	@Override
	public Object accept(GoIRVisitor visitor) {
		return visitor.visitKeyValue(this);
	}

	@Override
	public String getIdentifier(){
		return key.getIdentifier();
	}
	
	public GoBaseIRNode getKey() {
		return key;
	}

	public String getColon() {
		return colon;
	}

	public GoBaseIRNode getValue() {
		return value;
	}

}
