package com.oracle.app.nodes.SpecDecl;

import com.oracle.truffle.api.nodes.Node.Children;

public abstract class GoDecl {

	@Children GoGenDecl[] genDecl;
	//@Children GoFuncDecl[] genDecl;
}
