package com.oracle.app.parser.ir.nodes;

import java.util.ArrayList;

import com.oracle.app.parser.ir.GoBaseIRNode;
import com.oracle.app.parser.ir.GoIRVisitor;

public class GoIRDeclNode extends GoBaseIRNode {

	ArrayList<GoBaseIRNode> children;
	
	public GoIRDeclNode(ArrayList<GoBaseIRNode> children) {
		super("Decl");
		this.children = children;
	}
	
	@Override
	public Object accept(GoIRVisitor visitor) { 
		return visitor.visitDecl(this); 
	}
	
	public ArrayList<GoBaseIRNode> getChildren(){
		return children;
	}

	public int getSize() {
		return children.size();
	}

}
