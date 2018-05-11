package com.oracle.app.nodes.types;

import com.oracle.truffle.api.object.ObjectType;

/**
 *	Structs can be modeled off of a simple truffle shape where properties are added during
 *	type specs, then properties are only modified during selector expressions.
 *	Still need to read up on how the Truffle Object library works and see if Layouts could be of use
 * @author Trevor
 *
 */
public class GoStruct extends ObjectType{
    public static final ObjectType SINGLETON = new GoStruct();
    
    private GoStruct(){
    }
<<<<<<< HEAD
    
=======

    public void insertField(String key, FieldNode node){
        this.symbolTable.put(key, node);
        size++;
    }

    public Object executeGeneric(VirtualFrame frame){
        return this.deepCopy();
    }

    @Override
	public GoNonPrimitiveType doCompositeLit(VirtualFrame frame, Object[] vals) {
		if(vals.length != 0){
			if(vals[0] instanceof GoKeyValueNode){
				for(int i = 0; i < vals.length; i++){
				    //Not sure if you can simply call toString to an Object!!!!!
					write(((GoKeyValueNode) vals[i]).getKeyResult().toString(), ((GoKeyValueNode) vals[i]).getResult());
				}
			}
			else{
				if(vals.length != size){
					System.out.println("Too few values in struct initializer");
					return null;
				}
				int index = 0;
				for(FieldNode node : symbolTable.values()){
					node.insert(vals[index++]);
				}
			}
		}
		return this;
	}
>>>>>>> dade39d5038dbf03eb1462b225f54541ba92c189
}