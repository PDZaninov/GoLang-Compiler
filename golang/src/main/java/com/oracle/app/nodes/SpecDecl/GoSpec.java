package com.oracle.app.nodes.SpecDecl;

import com.oracle.app.nodes.GoExpressionNode;
import com.oracle.truffle.api.frame.VirtualFrame;

public abstract class GoSpec extends GoExpressionNode {

	@Children GoImportSpec[] nodes;
	
	@Override
	public Object executeGeneric(VirtualFrame frame) {
		// TODO Auto-generated method stub
		return null;
	}

}


//The Spec type stands for any of *ImportSpec, *ValueSpec, and *TypeSpec.
//
//type Spec interface {
//        Node
//        // contains filtered or unexported methods
//}