package com.oracle.app.parser.ir.nodes;

import com.oracle.app.parser.ir.GoBaseIRNode;
import com.oracle.app.parser.ir.nodes.GoIRBasicLitNode.GoIRIntNode;
import com.oracle.app.parser.ir.nodes.GoIRBasicLitNode.GoIRStringNode;

public class GoIRDefaultValues {

	public static GoIRBasicLitNode createDefaultBasicLits(GoIRIdentNode node){
		switch(node.getIdent()){
		case "int":
			return new GoIRIntNode(0);
		case "string":
			return new GoIRStringNode("");
		default:
			System.out.println("Unimplemented Default Value "+ node.getIdent());	
		}
		return null;
	}
	
	
}
