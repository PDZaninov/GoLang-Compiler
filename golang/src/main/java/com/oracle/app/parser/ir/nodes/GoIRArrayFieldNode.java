package com.oracle.app.parser.ir.nodes;

import java.util.ArrayList;

import com.oracle.app.parser.ir.GoBaseIRNode;
import com.oracle.app.parser.ir.GoIRVisitor;

public class GoIRArrayFieldNode extends GoBaseIRNode {

	ArrayList<GoBaseIRNode> children;
	
	public GoIRArrayFieldNode(ArrayList<GoBaseIRNode> children) {
		super("ArrayList Expression Node");
		this.children = children;
		setChildParent();
	}

	@Override
	public Object accept(GoIRVisitor visitor){
		return visitor.visitArrayField(this);
	}
	
	@Override
	public void setChildParent() {
		for(int x = 0; x < children.size(); x++){
			children.get(x).setParent(this);
		}
	}
	
	public void printChildren() {
		for(GoBaseIRNode child : children) {
			System.out.println("printing children of GoIRArrayListExprNode: " + child.toString());
		}
	}

	public int  getSize(){
		return children.size();
	}
	
	@Override
	public ArrayList<GoBaseIRNode> getChildren() {
		
		return children;
	}

}
