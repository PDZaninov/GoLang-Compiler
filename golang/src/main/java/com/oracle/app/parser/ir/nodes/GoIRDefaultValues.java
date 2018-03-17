package com.oracle.app.parser.ir.nodes;

public class GoIRDefaultValues {

	public static GoIRBasicLitNode createDefaultBasicLits(GoIRIdentNode node){
		switch(node.getIdent()){
		case "int":
			return new GoIRIntNode(0, null);
		case "string":
			return new GoIRStringNode("", null);
		default:
			System.out.println("Unimplemented Default Value "+ node.getIdent());	
		}
		return null;
	}
	
	
}
