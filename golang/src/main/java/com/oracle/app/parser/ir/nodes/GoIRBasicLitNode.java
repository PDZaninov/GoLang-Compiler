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
public abstract class GoIRBasicLitNode extends GoIRTypes {

	String type;
	String source;
	String val;
	
	public GoIRBasicLitNode(String source, String type, String value) {
		super("Basic Lit Node", type, value);
		this.source = source;
		this.type = type;
	}
	
	public String getType() {
		return type;
	}
	
	public abstract String getValString();
	
	/**
	 * Create a source section based off of the source file. In theory that should be the actual Gofile.
	 * Split the source by the colon and the first index is the line number
	 * The charindex is the right hand side. Might need to change it so that it can capture the whole number
	 * @param section The source code file
	 * @return The created source section
	 */
	public abstract SourceSection getSource(Source section);
	

	public static GoIRBasicLitNode createBasicLit(String name, String value,String source){
		switch(name){
			case "INT":
				return new GoIRIntNode(value,source);
			case "int":
				return new GoIRIntNode(value,source);
			case "STRING":
				return new GoIRStringNode(value,source);
			case "string":
				return new GoIRStringNode(value,source);
			case "float32":
				return new GoIRFloat32Node(value);
			case "float64":
				return new GoIRFloat64Node(value);
			case "FLOAT":
				return new GoIRFloat32Node(value);
			case "float":
				return new GoIRFloat32Node(value);
			default:
				System.out.println("Unimplemented Basic Lit type: " + name);
				return null;
		}
	
	}


}


