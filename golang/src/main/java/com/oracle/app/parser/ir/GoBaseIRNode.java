package com.oracle.app.parser.ir;

import java.util.ArrayList;

public abstract class GoBaseIRNode {
	
	protected String name;

    protected GoBaseIRNode parent;
    
    protected GoBaseIRNode child;
	
	public GoBaseIRNode(String name) { this.name = name; }
	
	public abstract void setChildParent();
	
	public abstract ArrayList<GoBaseIRNode> getChildren();
	
	public GoBaseIRNode getParent() { return parent; }
	
	public GoBaseIRNode getChild() { return child; }
	
	public void setParent(GoBaseIRNode node) { this.parent = node; }
	
	public void setChild(GoBaseIRNode node) { this.child = node; }
	
	public void accept(GoVisitor visitor) { visitor.visitObject(this); }
	
	public void accept(GoTruffle visitor) { visitor.visitObject(this); }
	
	public String toString() { return name; }
}