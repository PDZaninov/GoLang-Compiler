package com.oracle.app.parser.ir.nodes;

import java.util.ArrayList;

import com.oracle.app.parser.ir.GoBaseIRNode;

public class GoIRBasicLitNode extends GoBaseIRNode {

	private String type;
	private String value;
	
	public GoIRBasicLitNode(String type, String value) {
		super("Basic Lit Node");
		this.type = type;
		this.value = value;
	}
	
	//some accept function

	public String getType(){
		return type;
	}
	
	public String getValue(){
		return value;
	}
	
	@Override
	public void setChildParent() {
		// Do nothing :^)

	}

	@Override
	public ArrayList<GoBaseIRNode> getChildren() {
		return null;
	}

}
