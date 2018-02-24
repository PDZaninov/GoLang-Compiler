package com.oracle.app.parser.ir.nodes;

import java.util.ArrayList;

import com.oracle.app.parser.ir.GoBaseIRNode;

public class GoIRBasicLitNode extends GoBaseIRNode {

	String type;
	String value;
	
	public GoIRBasicLitNode(String type, String value) {
		super("Basic Lit Node");
		this.type = type;
		this.value = value;
	}
	
	//some accept function

	@Override
	public void setChildParent() {
		// Do nothing :^)

	}

	@Override
	public ArrayList<GoBaseIRNode> getChildren() {
		return null;
	}

}
