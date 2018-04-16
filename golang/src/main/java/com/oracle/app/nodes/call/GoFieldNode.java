package com.oracle.app.nodes.call;

import com.oracle.app.nodes.GoArrayExprNode;
import com.oracle.app.nodes.GoExpressionNode;
import com.oracle.app.nodes.GoIdentNode;
import com.oracle.truffle.api.frame.VirtualFrame;

public class GoFieldNode extends GoExpressionNode{

	@Child GoArrayExprNode names;
	@Child GoIdentNode type;
	
	String typeName;
	
	public GoFieldNode(GoArrayExprNode names, GoIdentNode type, String typeName) {
		this.names = names;
		this.type = type;
		this.typeName = typeName;
	}
	
	@Override
	public Object executeGeneric(VirtualFrame frame) {
		return names.executeGeneric(frame);
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