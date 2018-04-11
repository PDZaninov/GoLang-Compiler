package com.oracle.app.nodes.SpecDecl;

import com.oracle.app.nodes.GoExpressionNode;
import com.oracle.app.nodes.GoIdentNode;
import com.oracle.truffle.api.frame.VirtualFrame;

public class GoFieldNode extends GoExpressionNode{

	String type;
	@Child GoIdentNode[] Names;
	
	@Override
	public Object executeGeneric(VirtualFrame frame) {
		// TODO Auto-generated method stub
		return null;
	}
	
	
	
}
//type Field
//A Field represents a Field declaration list in a struct type, a method list in an interface type, or a parameter/result declaration in a signature.
//
//type Field struct {
//        Doc     *CommentGroup // associated documentation; or nil
//        Names   []*Ident      // field/method/parameter names; or nil if anonymous field
//        Type    Expr          // field/method/parameter type
//        Tag     *BasicLit     // field tag; or nil
//        Comment *CommentGroup // line comments; or nil
//}
//func (*Field) End
//func (f *Field) End() token.Pos
//func (*Field) Pos
//func (f *Field) Pos() token.Pos