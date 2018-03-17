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
	
	public GoIRBasicLitNode() {
		super("Basic Lit Node");
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

	public static GoIRBasicLitNode createBasicLit(String name, String value){
		switch(name){
		case "INT":
			return new GoIRIntNode(value);
		case "STRING":
			return new GoIRStringNode(value);
		default:
			System.out.println("Unimplemented Basic Lit type");
			return null;
		}
	
	}
}


