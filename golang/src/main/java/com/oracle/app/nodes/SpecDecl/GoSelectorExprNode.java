package com.oracle.app.nodes.SpecDecl;

import com.oracle.app.builtins.fmt.FmtFunctionList;
import com.oracle.app.nodes.GoExpressionNode;
import com.oracle.app.nodes.GoIdentNode;
import com.oracle.app.nodes.types.GoStruct;
import com.oracle.truffle.api.dsl.NodeChild;
import com.oracle.truffle.api.dsl.NodeChildren;
import com.oracle.truffle.api.dsl.Specialization;

@NodeChildren({@NodeChild(value="name"),@NodeChild(value="field",type=GoIdentNode.class)})
public abstract class GoSelectorExprNode extends GoExpressionNode {

	@Specialization
	public Object executeStruct(GoStruct struct, String field){
		return struct.read(field);
	}
	
	@Specialization
	public Object executeImport(FmtFunctionList imports, String function){
		return imports.getFunction(function);
	}
	
    //Only covering for the case of a struct selector currently
    //TO-DO Make sure this does not overlap with import selectors
	/*
    @Override
    public Object executeGeneric(VirtualFrame frame) {
    	GoExpressionNode result = (GoExpressionNode) expr.executeGeneric(frame);
    	if(result instanceof GoStruct){
    		return ((GoStruct) result).read(name.getName());
    	}
    	else{
    		//The selector expression variable was not found in the framedescriptor, error
    		System.out.println("Undefined selector");
    		return null;
    	}
    }
	*/
}
