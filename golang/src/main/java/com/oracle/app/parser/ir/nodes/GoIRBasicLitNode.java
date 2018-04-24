package com.oracle.app.parser.ir.nodes;

import com.oracle.app.parser.ir.GoBaseIRNode;
import com.oracle.truffle.api.source.Source;
import com.oracle.truffle.api.source.SourceSection;

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
	
	/**
	 * Create a source section based off of the source file. In theory that should be the actual Gofile.
	 * Split the source by the colon and the first index is the line number
	 * The charindex is the right hand side. Might need to change it so that it can capture the whole number
	 * @param section The source code file
	 * @return The created source section
	 */
	public abstract SourceSection getSource(Source section);
	
	public void changeType(String newType) {
		type = newType;
	}

	public static GoIRBasicLitNode createBasicLit(String name, String value,String source){
		switch(name){
			case "INT":
				return new GoIRIntNode(value,source);
			case "STRING":
				return new GoIRStringNode(value,source);
			case "FLOAT32":
				return new GoIRFloat32Node(value);
			case "FLOAT64":
				return new GoIRFloat64Node(value);
			case "FLOAT":
				return new GoIRFloat32Node(value);
			default:
				System.out.println("Unimplemented Basic Lit type");
				return null;
		}
	
	}

}


