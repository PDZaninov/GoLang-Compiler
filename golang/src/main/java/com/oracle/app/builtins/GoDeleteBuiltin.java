package com.oracle.app.builtins;

import com.oracle.app.nodes.types.GoMap;
import com.oracle.truffle.api.dsl.Specialization;

public abstract class GoDeleteBuiltin extends GoBuiltinNode {

	@Specialization
	public GoMap deleteValue(GoMap map, Object value){
		map.deleteElement(value);
		return map;
	}

}
