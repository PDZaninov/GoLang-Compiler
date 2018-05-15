package com.oracle.app.nodes.call;

import java.util.Arrays;

import com.oracle.app.nodes.GoExpressionNode;
import com.oracle.app.nodes.SpecDecl.GoSelectorExprNode;
import com.oracle.truffle.api.CompilerAsserts;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.instrumentation.StandardTags;
import com.oracle.truffle.api.nodes.ExplodeLoop;
import com.oracle.truffle.api.nodes.NodeInfo;
import com.oracle.truffle.api.object.DynamicObject;

@NodeInfo(shortName = "invoke")
public class GoInvokeNode extends GoExpressionNode {

    @Child protected GoExpressionNode functionNode;
    @Children protected final GoExpressionNode[] argumentNodes;
    @Child protected GoGenericDispatchNode dispatchNode;


    public GoInvokeNode(GoExpressionNode functionNode, GoExpressionNode[] argumentNodes) {
        this.functionNode = functionNode;
        this.argumentNodes = argumentNodes;
        this.dispatchNode = GoGenericDispatchNodeGen.create();
    }

    @ExplodeLoop
    @Override
    public Object executeGeneric(VirtualFrame frame) {
        Object function = functionNode.executeGeneric(frame);

        CompilerAsserts.compilationConstant(argumentNodes.length);
        Object[] argumentValues = new Object[argumentNodes.length];
        for (int i = 0; i < argumentNodes.length; i++) {
            argumentValues[i] = argumentNodes[i].executeGeneric(frame);
        }
        
        //This is used for struct methods. The struct passes itself as an argument to the method, but this is
        // a bad way of passing the struct to the function arguments.
        //TODO This should not be here. Think of a better place to insert the struct object into the arguments
        if(functionNode instanceof GoSelectorExprNode){
        	Object selector = ((GoSelectorExprNode) functionNode).getSelector(frame);
        	if(selector instanceof DynamicObject){
        		Object[] newargvalues = Arrays.copyOf(argumentValues, argumentNodes.length+1);
        		newargvalues[argumentNodes.length] = selector;
        		argumentValues = newargvalues;
        	}
        }
        
        return dispatchNode.executeDispatch(function, argumentValues);
    }

    @Override
    protected boolean isTaggedWith(Class<?> tag) {
        if (tag == StandardTags.CallTag.class) {
            return true;
        }
        return super.isTaggedWith(tag);
    }
    
}