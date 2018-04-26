package com.oracle.app.nodes.types;

import com.oracle.app.nodes.GoExpressionNode;
import com.oracle.truffle.api.frame.VirtualFrame;
import java.util.*
import com.oracle.app.nodes.expression.GoKeyValueNode;

public class GoMap extends GoArrayLikeTypes {

	protected Map<FieldNode, FieldNode> mapp;
	protected Object keyType;
	protected Object valueType;
	protected int size;

	public GoMap(Object keyType, Object valueType){
		this.keyType = keyType;
		this.valueType = valueType;
		this.mapp = new LinkedHashMap<>();
		size = 0;
	}

	@Override
	public int len(){
		return this.size;
	}

	@Override
	public Object read(FieldNode key){
		return this.table.get(key).read();
	}

	@Override
	public void insert(FieldNode key, FieldNode value){
		/** An issue could be that Java hashmap cannot compare (.equals()) on abstract types like key which is a fieldNode
		 *	Also when you insert the value read from the parameter fieldNode value, it might be a wrong type since we have no type checking.
		 */
		if (this.mapp.containsKey(key)){
			this.mapp.get(key).insert(value.read());
		} else {
			this.mapp.put(key, value);
			size++;
		}
	}

	@Override
	public Object executeGeneric(VirtualFrame frame) {
		return this.deepCopy();
	}

	@Override
	public GoArrayLikeTypes doCompositeLit(VirtualFrame frame, Object[] vals) {
		/**	I'm assuming that Object[] vals only contains a list of KeyValueExprNodes from which I'll extract fieldNodes.
		 *	So as of right now we do not have a good way of checking the data types of the inserted key and value.
		 *	When I create the fieldnodes "key" and "value", i put the data type as null for now -> This should be figured out soon.
		 */
		if(vals.length != 0){
			for(int i = 0; i < vals.length; i++) {
				FieldNode key = new FieldNode(((GoKeyValueNode) vals[i]).getKeyResult(), null);
				FieldNode value = new FieldNode(((GoKeyValueNode) vals[i]).getResult(), null);
				this.insert(key, value);
			}
		}
		return this;
	}

}
