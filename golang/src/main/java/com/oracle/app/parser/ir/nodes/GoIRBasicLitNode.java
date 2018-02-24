package com.oracle.app.parser.ir.nodes;

import java.util.ArrayList;

import com.oracle.app.parser.ir.GoBaseIRNode;
import com.oracle.app.parser.ir.GoTruffle;
import com.oracle.app.parser.ir.GoVisitor;

public class GoIRBasicLitNode extends GoBaseIRNode {

	private String type;
	private String value;
	
	public GoIRBasicLitNode(String type, String value) {
		super("Basic Lit Node");
		this.type = type;
		this.value = value;
	}
	
	public String getType() {
		return type;
	}
	
	public String getValue() {
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
	
	@Override
	public void accept(GoVisitor visitor) { 
		visitor.visitBasicLit(this); 
	}
	
	@Override
	public void accept(GoTruffle visitor) { 
		visitor.visitBasicLit(this); 
	}

}
