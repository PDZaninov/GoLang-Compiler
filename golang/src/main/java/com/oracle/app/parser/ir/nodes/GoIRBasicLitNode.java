package com.oracle.app.parser.ir.nodes;

import java.util.ArrayList;

import com.oracle.app.parser.ir.GoBaseIRNode;

/**
 * Abstract BasicLiteral node class for any basic type node.
 * Create basic IR nodes using the static createBasicLit method so that the 
 * type handling is dealt with early on. When creating Truffle Nodes the type can
 * be created in one line.
 * Missing type:
 * 	FLOAT
 * 	IMAG
 * 	CHAR
 * @author Trevor
 *
 */
public abstract class GoIRBasicLitNode extends GoBaseIRNode {

	String type;
	String source;
	
	public GoIRBasicLitNode(String source) {
		super("Basic Lit Node");
		this.source = source;
	}
	
	public String getType() {
		return type;
	}
	
	@Override
	public void setChildParent() {
	}

	@Override
	public ArrayList<GoBaseIRNode> getChildren() {
		return null;
	}

	public static GoIRBasicLitNode createBasicLit(String name, String value,String source){
		switch(name){
		case "INT":
			return new GoIRIntNode(value,source);
		case "STRING":
			return new GoIRStringNode(value, source);
		default:
			System.out.println("Unimplemented Basic Lit type");
			return null;
		}
	
	}

}


