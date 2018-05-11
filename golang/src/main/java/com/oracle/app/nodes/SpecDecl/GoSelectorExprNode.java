package com.oracle.app.nodes.SpecDecl;

import com.oracle.app.builtins.fmt.FmtFunctionList;
import com.oracle.app.nodes.GoExpressionNode;
import com.oracle.app.nodes.GoIdentNode;
import com.oracle.app.nodes.local.GoReadPropertyNode;
import com.oracle.app.nodes.local.GoReadPropertyNodeGen;
import com.oracle.truffle.api.dsl.NodeChild;
import com.oracle.truffle.api.dsl.NodeChildren;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.object.DynamicObject;

@NodeChildren({@NodeChild(value="name"),@NodeChild(value="field",type=GoIdentNode.class)})
public abstract class GoSelectorExprNode extends GoExpressionNode {
	
	@Specialization
	public Object executeStruct(DynamicObject struct, String field){
		GoReadPropertyNode property = GoReadPropertyNodeGen.create(); 
		return property.doRead(struct, field);
	}
	
	@Specialization
	public Object executeImport(FmtFunctionList imports, String function){
		return imports.getFunction(function);
	}
	
}
