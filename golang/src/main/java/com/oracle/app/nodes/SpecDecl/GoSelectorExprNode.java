package com.oracle.app.nodes.SpecDecl;

import com.oracle.app.builtins.fmt.FmtFunctionList;
import com.oracle.app.builtins.time.GoTimeNow;
import com.oracle.app.builtins.time.TimeFunctionList;
import com.oracle.app.nodes.GoExpressionNode;
import com.oracle.app.nodes.GoIdentNode;
import com.oracle.app.nodes.local.GoReadPropertyNode;
import com.oracle.app.nodes.local.GoReadPropertyNodeGen;
import com.oracle.truffle.api.dsl.NodeChild;
import com.oracle.truffle.api.dsl.NodeChildren;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.object.DynamicObject;

@NodeChildren({@NodeChild(value="varname"),@NodeChild(value="field",type=GoIdentNode.class)})
public abstract class GoSelectorExprNode extends GoExpressionNode {
	
	public abstract GoExpressionNode getVarname();
	
	@Specialization
	public Object executeStruct(DynamicObject struct, String field){
		GoReadPropertyNode property = GoReadPropertyNodeGen.create(); 
		return property.doRead(struct, field);
	}
	
	@Specialization
	public Object executeImport(FmtFunctionList imports, String function){
		return imports.getFunction(function);
	}
	
	@Specialization
	public Object executeImport(TimeFunctionList imports, String function){
		return imports.getFunction(function);
	}
	//Brute forced timer function. Used to time the go program assuming the function is
	//UnixNano, EpochSecond is the most equivalent.
	@Specialization
	public Object executeTimer(GoTimeNow timer, String function){
		
		return timer.getUnixNano();
	}

	public Object getSelector(VirtualFrame frame){
		return getVarname().executeGeneric(frame);
	}
	
}
