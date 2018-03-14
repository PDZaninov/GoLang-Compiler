package com.oracle.app.parser.ir.nodes;

import java.util.ArrayList;

import com.oracle.app.parser.ir.GoBaseIRNode;
import com.oracle.app.parser.ir.GoIRVisitor;

public class GoIRIdentNode extends GoBaseIRNode {
	
	private String ident;
	
	private GoBaseIRNode child;
	
	public GoIRIdentNode(String ident, GoBaseIRNode child) {
		super("Ident");
		this.ident = ident;
		this.child = child;
		setChildParent();
	}
	
	@Override
	public void setChildParent() {
		if(child != null)
			child.setParent(this);
	}
	
	@Override
	public ArrayList<GoBaseIRNode> getChildren() {
		return null;
	}
	
	//Will need to merge getIdent and getIdentifier so that only getIdentifier is used
	@Override
	public String getIdentifier(){
		return ident;
	}
	
	public String getIdent() {
		return ident;
	}
	
	public GoBaseIRNode getChild() {
		return child;
	}
	
	@Override
	public Object accept(GoIRVisitor visitor) { 
		return visitor.visitIdent(this); 
	}
	
	
}
