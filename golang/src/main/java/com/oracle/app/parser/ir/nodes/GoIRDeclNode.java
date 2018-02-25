package com.oracle.app.parser.ir.nodes;

import java.util.ArrayList;

import com.oracle.app.parser.ir.GoBaseIRNode;
import com.oracle.app.parser.ir.GoIRVisitor;

public class GoIRDeclNode extends GoBaseIRNode {

	ArrayList<GoBaseIRNode> children;
	
	public GoIRDeclNode(ArrayList<GoBaseIRNode> children) {
		super("Decl");
		this.children = children;
		setChildParent();
	}

	@Override
	public void setChildParent() {
		for(int i = 0; i < children.size(); i++)
			this.children.get(i).setParent(this);
	}

	@Override
	public ArrayList<GoBaseIRNode> getChildren() {
		return children;
	}
	
	@Override
	public void accept(GoIRVisitor visitor) { 
		visitor.visitDecl(this); 
	}

}
