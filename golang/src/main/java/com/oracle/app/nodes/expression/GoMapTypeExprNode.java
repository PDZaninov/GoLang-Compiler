package com.oracle.app.nodes.expression;

import com.oracle.app.nodes.GoExpressionNode;
import com.oracle.app.nodes.GoIdentNode;
import com.oracle.app.nodes.call.GoFieldNode;
import com.oracle.app.nodes.local.GoReadLocalVariableNode;
import com.oracle.app.nodes.types.FieldNode;
import com.oracle.app.nodes.types.GoMap;
import com.oracle.truffle.api.frame.VirtualFrame;

public class GoMapTypeExprNode extends GoExpressionNode{
    GoExpressionNode keyType;
    GoExpressionNode valueType;

    public GoMapTypeExprNode(GoExpressionNode keyType, GoExpressionNode valueType){
        this.keyType = keyType;
        this.valueType = valueType;
    }

    public Object executeGeneric(VirtualFrame frame){

        GoMap result = new GoMap(keyType.executeGeneric(frame), valueType.executeGeneric(frame));
        return result;
    }

}