package com.oracle.app.nodes.expression;

import com.oracle.app.nodes.GoExpressionNode;
import com.oracle.app.nodes.GoIdentNode;
import com.oracle.app.nodes.call.GoFieldNode;
import com.oracle.app.nodes.local.GoReadLocalVariableNode;
import com.oracle.app.nodes.types.FieldNode;
import com.oracle.app.nodes.types.GoStruct;
import com.oracle.truffle.api.frame.VirtualFrame;

public class GoStructTypeExprNode extends GoExpressionNode{
    GoFieldNode[] fields;

    public GoStructTypeExprNode(GoFieldNode[] fields){
        this.fields = fields;
    }

    public Object executeGeneric(VirtualFrame frame){
        GoStruct result = new GoStruct();
        for(GoFieldNode child : fields){
            GoReadLocalVariableNode type = child.getType();
            GoIdentNode name = (GoIdentNode) child.getNames()[0]; // This is the name of the variable
            //type.execute = Object value of the type field, type.name = the type name such as int
            FieldNode field = new FieldNode(type.executeGeneric(frame), (String) type.getSlot().getIdentifier());
            result.insertField(name.getName(),field);
        }
        return result;
    }
}