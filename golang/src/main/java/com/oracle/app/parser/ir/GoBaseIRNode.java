package com.oracle.app.parser.ir;

import java.util.ArrayList;

public abstract class GoBaseIRNode implements GoIRVisitable {
	
	protected String name;
	
	public GoBaseIRNode(String name) { this.name = name; }
	public String toString() { return name; }
	public String getIdentifier(){ return name; }
	
	public abstract Object accept(GoIRVisitor visitor);
	
	
}