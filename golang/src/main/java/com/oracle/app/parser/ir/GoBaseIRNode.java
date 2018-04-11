package com.oracle.app.parser.ir;

import com.oracle.truffle.api.source.Source;
import com.oracle.truffle.api.source.SourceSection;

public abstract class GoBaseIRNode implements GoIRVisitable {
	
	protected String name;
	
	public GoBaseIRNode(String name) { this.name = name; }
	public String toString() { return name; }
	public String getIdentifier(){ return name; }

	public abstract Object accept(GoIRVisitor visitor);
	
	
}