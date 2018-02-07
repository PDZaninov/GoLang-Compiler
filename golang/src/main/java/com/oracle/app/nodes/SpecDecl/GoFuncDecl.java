package com.oracle.app.nodes.SpecDecl;

import com.oracle.app.nodes.controlflow.GoBlockNode;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.Node.Child;

public class GoFuncDecl extends GoDeclNode {

	@Child GoBlockNode Body;
	
	@Override
	public Object executeGeneric(VirtualFrame frame) {
		// TODO Auto-generated method stub
		return null;
	}
	
	
}
//A FuncDecl node represents a function declaration.
//
//type FuncDecl struct {
//        Doc  *CommentGroup // associated documentation; or nil
//        Recv *FieldList    // receiver (methods); or nil (functions)
//        Name *Ident        // function/method name
//        Type *FuncType     // function signature: parameters, results, and position of "func" keyword
//        Body *BlockStmt    // function body; or nil for external (non-Go) function
//}
//func (*FuncDecl) End
//func (d *FuncDecl) End() token.Pos
//func (*FuncDecl) Pos
//func (d *FuncDecl) Pos() token.Pos