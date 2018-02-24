package com.oracle.app.parser.ir;

import java.util.ArrayList;

public abstract class GoBaseIRNode {
	
	protected String name;

    protected GoBaseIRNode parent;
	
	public GoBaseIRNode(String name) { this.name = name; }
	
	public abstract void setChildParent();
	
	public abstract ArrayList<GoBaseIRNode> getChildren();
	
	public GoBaseIRNode getParent() { return parent; }
	
	public void setParent(GoBaseIRNode node) { this.parent = node; }
	
	public void accept(GoIRVisitor visitor) { visitor.visitObject(this); }
	
	public String toString() { return name; }
}