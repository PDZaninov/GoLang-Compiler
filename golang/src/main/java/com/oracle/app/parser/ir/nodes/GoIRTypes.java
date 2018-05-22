package com.oracle.app.parser.ir.nodes;

import com.oracle.app.parser.ir.GoBaseIRNode;

public abstract class GoIRTypes extends GoBaseIRNode {

	String type;
	String value;
	
	public GoIRTypes(String name, String type, String value) {
		super(name);
		this.type = type;
	}
	
	public String getValueType() {
		return type;
	}
	
	public String getValueString() {
		return value;
	}

}
