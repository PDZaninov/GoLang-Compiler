package com.oracle.app.builtins;

import com.oracle.app.nodes.types.GoArray;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.nodes.NodeInfo;

@NodeInfo(shortName = "len")
public abstract class GoLenBuiltin extends GoBuiltinNode{

	@Specialization
	public int len(GoArray a) {
		return a.len(a);
	}

}
