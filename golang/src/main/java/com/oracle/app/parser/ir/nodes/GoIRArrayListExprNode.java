package com.oracle.app.parser.ir.nodes;

import java.util.ArrayList;

import com.oracle.app.parser.ir.GoBaseIRNode;
import com.oracle.app.parser.ir.GoIRVisitor;

public class GoIRArrayListExprNode extends GoBaseIRNode {

	ArrayList<GoBaseIRNode> children;
	String source;
	
	public GoIRArrayListExprNode(ArrayList<GoBaseIRNode> children,String source) {
		super("ArrayList Expression Node");
		this.children = children;
		this.source = source;
	}
	
	public GoIRArrayListExprNode(ArrayList<GoBaseIRNode> children) {
		super("ArrayList Expression Node");
		this.children = children;
		this.source = null;
	}

	@Override
	public Object accept(GoIRVisitor visitor){
		return visitor.visitArrayListExpr(this);
	}
	
	public void printChildren() {
		for(GoBaseIRNode child : children) {
			System.out.println("printing children of GoIRArrayListExprNode: " + child.toString());
		}
	}

	public int  getSize(){
		return children.size();
	}
	
	public ArrayList<GoBaseIRNode> getChildren() {
		
		return children;
	}

}
