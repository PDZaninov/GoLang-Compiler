package com.oracle.app.nodes.SpecDecl;


import com.oracle.app.nodes.GoExpressionNode;
import com.oracle.truffle.api.frame.VirtualFrame;

public class GoGenDecl extends GoExpressionNode {
	
	@Children GoSpec[] spec;
	int Lparen;
	int Rparen;
	String type;//        Tok    token.Token   // IMPORT, CONST, TYPE, VAR
	int TokPos;
	
	public GoGenDecl(int l, int r, String t, int pos) {
		Lparen = l;
		Rparen = r;
		type = t;
		TokPos = pos;
	}
	
	@Override
	public Object executeGeneric(VirtualFrame frame) {
		// TODO Auto-generated method stub
		return null;
	}
	
	public int End(GoGenDecl d) {
		return Rparen;
	}
	
	public int Pos(GoGenDecl d) {
		return Lparen;
	}
	
//	A GenDecl node (generic declaration node) represents an import, constant, type or variable declaration.
	//A valid Lparen position (Lparen.IsValid()) indicates a parenthesized declaration.
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
//  Tok    token.Token   // IMPORT, CONST, TYPE, VAR
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
