package com.oracle.app.parser.ir.nodes;

import java.util.ArrayList;

import com.oracle.app.parser.ir.GoBaseIRNode;
import com.oracle.app.parser.ir.GoTruffle;
import com.oracle.app.parser.ir.GoVisitor;

public class GoIRIdentNode extends GoBaseIRNode {
	
	private String ident;
	
	public GoIRIdentNode(String ident, GoBaseIRNode child) {
		super("Ident");
		this.ident = ident;
		this.setChild(child); 
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
	public void accept(GoVisitor visitor) { 
		visitor.visitIdent(this); 
	}
	
	@Override
	public void accept(GoTruffle visitor) { 
		visitor.visitIdent(this); 
	}
	
}
