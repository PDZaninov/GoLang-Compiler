package com.oracle.app.nodes;

import com.oracle.app.GoLanguage;
import com.oracle.app.runtime.GoContext;
import com.oracle.app.runtime.GoFunction;
import com.oracle.truffle.api.TruffleLanguage.ContextReference;
import com.oracle.truffle.api.frame.VirtualFrame;

public class GoIdentNode extends GoExpressionNode{
	
	String name;
	String[] attr = new String[25];
	public GoBasicNode parent;
	@Children final private GoStatementNode[] children;
	
	private final ContextReference<GoContext> reference;
	
	public GoIdentNode(GoLanguage language, String name, GoStatementNode[] children) {
		this.name = name;
		this.children = children;
		reference = language.getContextReference();
	}

	@Override
	public String toString() {
		return name;
	}
	
	public GoFunction getFunction(){
		return reference.get().getFunctionRegistry().lookup(name, true);
	}
	
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