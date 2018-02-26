package com.oracle.app.parser.ir.nodes;

import java.util.ArrayList;

import com.oracle.app.parser.ir.GoBaseIRNode;
import com.oracle.app.parser.ir.GoIRVisitor;

public class GoIRStmtNode extends GoBaseIRNode {
	
	private ArrayList<GoBaseIRNode> children;
	
	public GoIRStmtNode(ArrayList<GoBaseIRNode> children) {
		super("Stmt");
		this.children = children;
		setChildParent();
	}
	
	public int getSize(){
		return children.size();
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
	
	
	@Override
	public Object accept(GoIRVisitor visitor) { 
		return visitor.visitStmt(this); 
	}
	
	
}
