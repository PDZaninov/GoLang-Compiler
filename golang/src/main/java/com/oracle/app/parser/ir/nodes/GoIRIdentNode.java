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
		child.setParent(this);
	}
	
	@Override
	public ArrayList<GoBaseIRNode> getChildren() {
		// TODO Auto-generated method stub
		return null;
	}
	
	public String getIdent() {
		return ident;
	}
	
	@Override
	public void accept(GoIRVisitor visitor) { 
		visitor.visitIdent(this); 
	}
	
	
}
