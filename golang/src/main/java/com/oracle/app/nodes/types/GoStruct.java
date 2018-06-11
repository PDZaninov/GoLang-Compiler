package com.oracle.app.nodes.types;

import com.oracle.truffle.api.object.ObjectType;
import com.oracle.truffle.api.object.Property;
import com.oracle.truffle.api.object.Shape;

/**
 *	Structs can be modeled off of a simple truffle shape where properties are added during
 *	type specs, then properties are only modified during selector expressions.
 *	Still need to read up on how the Truffle Object library works and see if Layouts could be of use
 * @author Trevor
 *
 */
public class GoStruct extends ObjectType{
    public static final ObjectType SINGLETON = new GoStruct();
    public static final int STRUCT_FIELD = 0;
	public static final int STRUCT_METHOD = 1;
    
    private GoStruct(){
    }
    
    public static Shape.Pred<Property> getKeyList(){
		return p -> p.getFlags() == STRUCT_FIELD;
	}
}