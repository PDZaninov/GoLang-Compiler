package com.oracle.app.parser.ir.nodes;

import java.util.ArrayList;

import com.oracle.app.parser.ir.GoBaseIRNode;
import com.oracle.app.parser.ir.GoIRVisitor;

public class GoIRArrayListExprNode extends GoBaseIRNode {

	ArrayList<GoBaseIRNode> children;
	
	public GoIRArrayListExprNode(ArrayList<GoBaseIRNode> children) {
		super("ArrayList Expression Node");
		this.children = children;
		setChildParent();
	}

	@Override
	public void accept(GoIRVisitor visitor){
		visitor.visitArrayListExpr(this);
	}
	
	@Override
	public void setChildParent() {
		if(children.isEmpty())
		for(GoBaseIRNode child : children){
			child.setParent(this);
		}

	}

	@Override
	public ArrayList<GoBaseIRNode> getChildren() {
		
		return children;
	}

}
