package com.oracle.app.nodes.call;

import com.oracle.app.nodes.GoExpressionNode;
import com.oracle.truffle.api.frame.VirtualFrame;
// probably should extend something else
public class GoFieldListNode extends GoExpressionNode {

	@Children GoFieldNode[] nodes;
	
	
	@Override
	public Object executeGeneric(VirtualFrame frame) {
		// TODO Auto-generated method stub
		return null;
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