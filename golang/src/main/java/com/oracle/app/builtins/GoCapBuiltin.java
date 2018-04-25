package com.oracle.app.builtins;

import com.oracle.app.nodes.types.GoArrayLikeTypes;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.nodes.NodeInfo;
 
@NodeInfo(shortName = "cap")
public abstract class GoCapBuiltin extends GoBuiltinNode{

	@Specialization
	public int cap(GoArrayLikeTypes array){
		return array.cap();
	}

}
