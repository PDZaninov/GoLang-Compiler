package com.oracle.app.parser.ir.nodes;

import java.util.ArrayList;

import com.oracle.app.parser.ir.GoBaseIRNode;

public class GoIRArrayListExprNode extends GoBaseIRNode {

	ArrayList<GoBaseIRNode> children;
	
	public GoIRArrayListExprNode(ArrayList<GoBaseIRNode> children) {
		super("ArrayList Expression Node");
		this.children = children;
	}

	@Override
	public void setChildParent() {
		for(GoBaseIRNode child : children){
			child.setParent(this);
		}

	}

	@Override
	public ArrayList<GoBaseIRNode> getChildren() {
		
		return children;
	}

}
