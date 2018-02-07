package com.oracle.app.nodes;

import com.oracle.truffle.api.frame.VirtualFrame;

public class GoGenDecl extends GoExpressionNode {
	
	@Children GoSpec[] spec;
	
	@Override
	public Object executeGeneric(VirtualFrame frame) {
		// TODO Auto-generated method stub
		return null;
	}
	
//	A GenDecl node (generic declaration node) represents an import, constant, type or variable declaration. A valid Lparen position (Lparen.IsValid()) indicates a parenthesized declaration.
//
//	Relationship between Tok value and Specs element type:
//
//	token.IMPORT  *ImportSpec
//	token.CONST   *ValueSpec
//	token.TYPE    *TypeSpec
//	token.VAR     *ValueSpec
	
//	type GenDecl struct {
//        Doc    *CommentGroup // associated documentation; or nil
//        TokPos token.Pos     // position of Tok
//        Tok    token.Token   // IMPORT, CONST, TYPE, VAR
//        Lparen token.Pos     // position of '(', if any
//        Specs  []Spec
//        Rparen token.Pos // position of ')', if any
//}
//	
//
//func (*GenDecl) End
//func (d *GenDecl) End() token.Pos
//func (*GenDecl) Pos
//func (d *GenDecl) Pos() token.Pos

}
