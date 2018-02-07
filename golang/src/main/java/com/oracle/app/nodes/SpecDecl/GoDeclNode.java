package com.oracle.app.nodes.SpecDecl;

import com.oracle.app.nodes.GoExpressionNode;
import com.oracle.truffle.api.nodes.Node.Children;

public abstract class GoDeclNode extends GoExpressionNode {

	@Children GoGenDecl[] genDecl;
	//@Children GoFuncDecl[] genDecl;
}
