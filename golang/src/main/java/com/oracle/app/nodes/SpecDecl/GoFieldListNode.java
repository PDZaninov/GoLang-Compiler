package com.oracle.app.nodes.SpecDecl;

import com.oracle.app.nodes.GoExpressionNode;
import com.oracle.truffle.api.frame.VirtualFrame;
// probably should extend something else
public class GoFieldListNode extends GoExpressionNode {

	int Opening;
	int Closing;
	@Children GoFieldNode[] nodes;
	
	
	@Override
	public Object executeGeneric(VirtualFrame frame) {
		// TODO Auto-generated method stub
		return null;
	}
	
	public GoFieldListNode(int o, int c, GoFieldNode[] g) {
		Opening = o;
		Closing = c;
		nodes = g;
	}
	
	public int End(GoFieldListNode g) {
		return Closing;
	}

	public int Pos(GoFieldListNode g) {
		return Opening;
	}
	
	
}
//A FieldList represents a list of Fields, enclosed by parentheses or braces.
//
//type FieldList struct {
//        Opening token.Pos // position of opening parenthesis/brace, if any
//        List    []*Field  // field list; or nil
//        Closing token.Pos // position of closing parenthesis/brace, if any
//}
//func (*FieldList) End
//func (f *FieldList) End() token.Pos
//func (*FieldList) NumFields
//func (f *FieldList) NumFields() int
//NumFields returns the number of (named and anonymous fields) in a FieldList.
//
//func (*FieldList) Pos
//func (f *FieldList) Pos() token.Pos