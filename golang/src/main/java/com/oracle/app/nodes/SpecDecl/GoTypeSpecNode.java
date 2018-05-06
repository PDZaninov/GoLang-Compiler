package com.oracle.app.nodes.SpecDecl;

import com.oracle.app.GoLanguage;
import com.oracle.app.nodes.GoExpressionNode;
import com.oracle.app.nodes.local.GoWriteLocalVariableNode;
import com.oracle.app.runtime.GoContext;
import com.oracle.truffle.api.frame.VirtualFrame;

/**
 * TypeSpec node creates the struct object and inserts it into the frame stack.
 * StructProperties will fill in the fields for the struct
 * I don't think this works the way I want it to....
 * @author Trevor
 *
 */
public class GoTypeSpecNode extends GoExpressionNode {

	private final GoWriteLocalVariableNode structWrite;
	private final GoExpressionNode structProperties;
	
	public static class GoNewStruct extends GoExpressionNode{

		@Override
		public Object executeGeneric(VirtualFrame frame){
			return getContext().createStruct();
		}
		
		public final GoContext getContext() {
	        return getRootNode().getLanguage(GoLanguage.class).getContextReference().get();
	    }
		
	}
	
	public GoTypeSpecNode(GoWriteLocalVariableNode structWrite, GoExpressionNode structProperties) {
		this.structWrite = structWrite;
		this.structProperties = structProperties;
	}

	@Override
	public Object executeGeneric(VirtualFrame frame) {
		structWrite.executeGeneric(frame);
		return structProperties.executeGeneric(frame);
	}

}
