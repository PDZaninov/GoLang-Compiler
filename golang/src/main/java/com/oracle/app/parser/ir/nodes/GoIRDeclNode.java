package com.oracle.app.parser.ir.nodes;

import java.util.ArrayList;

import com.oracle.app.parser.ir.GoBaseIRNode;
import com.oracle.app.parser.ir.GoIRVisitor;

public class GoIRDeclNode extends GoBaseIRNode {

	GoBaseIRNode[] children;
	
	public GoIRDeclNode(GoBaseIRNode[] children) {
		super("Decl");
		this.children = children;
		setChildParent();
	}

	@Override
	public void setChildParent() {
		for(int i = 0; i < children.length; i++)
			this.children[i].setParent(this);
	}

	@Override
	public ArrayList<GoBaseIRNode> getChildren() {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public void accept(GoIRVisitor visitor) { 
		visitor.visitDecl(this); 
	}

}
