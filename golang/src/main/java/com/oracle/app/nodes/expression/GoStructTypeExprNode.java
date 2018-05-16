package com.oracle.app.nodes.expression;

import com.oracle.app.GoLanguage;
import com.oracle.app.nodes.GoExpressionNode;
import com.oracle.app.nodes.call.GoFieldNode;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.object.DynamicObject;
import com.oracle.truffle.api.object.DynamicObjectFactory;
import com.oracle.truffle.api.object.Shape;

/**
 * Creates the struct dynamic object and defines its fields. Returns the shape of the struct.
 * Currently fields can only be created if the field is on its own line.
 * TODO Change FieldNodes, possibly make struct a Shape rather then a DynamicObject
 * @author Trevor
 *
 */
public class GoStructTypeExprNode extends GoExpressionNode{
    GoFieldNode[] fields;
    DynamicObject struct = getNewStruct();

    public GoStructTypeExprNode(GoFieldNode[] fields){
        this.fields = fields;
    }

    public Object executeGeneric(VirtualFrame frame){

        for(GoFieldNode child : fields){
            Object type = child.getType().executeGeneric(frame);
            String name = child.getName(); // This is the name of the field
            struct.define(name, type);
            /*
            FieldNode field = new FieldNode(type.executeGeneric(frame), (String) type.getSlot().getIdentifier());

            //If name is null (check fieldNode handler function in GoTruffle)
            if(name != null){
                result.insertField(name.getName(),field);
            } else {
                result.insertField((String) type.getSlot().getIdentifier(), field);
            }
            */
        }
    	return struct.getShape();
    }
    
    public DynamicObject getNewStruct(){
    	return GoLanguage.getCurrentContext().createStruct();
    }
}