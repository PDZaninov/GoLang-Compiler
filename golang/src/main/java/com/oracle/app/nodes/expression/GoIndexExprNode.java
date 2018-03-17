package com.oracle.app.nodes.expression;

import com.oracle.app.nodes.GoExpressionNode;
import com.oracle.app.nodes.local.GoReadLocalVariableNode;
import com.oracle.app.nodes.types.GoArray;
import com.oracle.truffle.api.frame.VirtualFrame;

/**
 * This node is really just used for the functions. Not to be executed
 * Need to find a better way to use the functions inside the execution function
 * @author Trevor
 *
 */
public class GoIndexExprNode extends GoExpressionNode{
	
	/*The array variable is a read node because calling an index expression 
	 * you have to get the GoArray object from a read local variable slot*/
	private GoReadLocalVariableNode array;
	GoExpressionNode index;
	
	public GoIndexExprNode(GoReadLocalVariableNode array,GoExpressionNode index){
		this.array = array;
		this.index = index;
	}
	
	public GoReadLocalVariableNode getName(){
		return array;
	}
	
	public GoExpressionNode getIndex(){
		return index;
	}
	
    @Override
	public Object executeGeneric(VirtualFrame frame) {
		//Don't know about this yet.
		return null;
	}
    
    protected Object doIndex(GoArray array, int index) {
        return array.readArray(index);
    }
    
	@Override
	public String toString() {
		return "GoIndexExprNode []";
	}
    
}
