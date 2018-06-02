package com.oracle.app.nodes.expression;

import java.util.List;

import com.oracle.app.nodes.GoBinaryNode;
import com.oracle.app.nodes.types.GoArray;
import com.oracle.app.nodes.types.GoStruct;
import com.oracle.app.runtime.GoFunction;
import com.oracle.app.runtime.GoNull;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.nodes.NodeInfo;
import com.oracle.truffle.api.object.DynamicObject;
import com.oracle.truffle.api.object.Shape;

@NodeInfo(shortName = "!=")
public abstract class GoNotEqualNode extends GoBinaryNode {

    @Specialization
    protected boolean  notEqual(int left, int right) {
        return left != right;
    }

    @Specialization
    protected boolean  notEqual(boolean left, boolean right) {
        return left != right;
    }

    @Specialization
    protected boolean  notEqual(String left, String right) {
        return !left.equals(right);
    }
    
    @Specialization
    protected boolean notEqual(GoArray left, GoArray right){
    	//Need to be equal type
    	if(!left.compare(right)){
    		return false;
    	}
    	//Then check each value
    	for(int i = 0; i < left.len(); i++){
    		if(left.read(i) != right.read(i)){
    			return true;
    		}
    	}
    	return false;
    }
    
    @Specialization
    protected boolean notEqual(DynamicObject left, DynamicObject right){
    	Shape leftshape = left.getShape();
    	Shape rightshape = right.getShape();
    	if(leftshape != rightshape){
    		return false;
    	}
    	List<Object> keylist = leftshape.getKeyList(GoStruct.getKeyList());
    	for(Object key : keylist){
    		if(left.get(key) != right.get(key)){
    			return true;
    		}
    	}
    	return false;
    }

    @Specialization
    protected boolean  notEqual(GoFunction left, GoFunction right) {
        return left != right;
    }

    @Specialization
    protected boolean  notEqual(GoNull left, GoNull right) {
        return left != right;
    }

}
