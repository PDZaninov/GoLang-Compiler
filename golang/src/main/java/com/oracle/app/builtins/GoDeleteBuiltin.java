package com.oracle.app.builtins;

import com.oracle.app.nodes.types.FieldNode;
import com.oracle.app.nodes.types.GoMap;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.nodes.NodeInfo;

@NodeInfo(shortName = "delete")
public abstract class GoDeleteBuiltin extends GoBuiltinNode {

	@Specialization
	public GoMap deleteValue(GoMap map, Object value){
		map.deleteElement(new FieldNode(value,null));
		return map;
	}

}
