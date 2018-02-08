package com.oracle.app.nodes;

import com.oracle.truffle.api.frame.VirtualFrame;

public class GoIdentNode extends GoExpressionNode{

	@Override
	public Object executeGeneric(VirtualFrame frame) {
		// TODO Auto-generated method stub
		return null;
	}
	
	

}
//type Ident struct {
//    NamePos token.Pos // identifier position
//    Name    string    // identifier name
//    Obj     *Object   // denoted object; or nil
//}