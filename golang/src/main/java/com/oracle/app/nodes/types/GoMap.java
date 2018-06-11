package com.oracle.app.nodes.types;

import java.util.LinkedHashMap;
import java.util.Map;

import com.oracle.app.nodes.expression.GoKeyValueNode;
import com.oracle.truffle.api.frame.VirtualFrame;

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
	
	private GoMap(Object keyType, Object valueType, Map<FieldNode,FieldNode> mapp){
		this.keyType = keyType;
		this.valueType = valueType;
		this.mapp = mapp;
		size = 0;
	}
	
	public void setNewMapCapacity(int capacity){
		mapp = new LinkedHashMap<>(capacity);
	}

	public boolean fieldExist(FieldNode key){
		String value = key.toString();
		for(FieldNode k : this.mapp.keySet()){
			if(k.toString().equals(value)){
				return true;
			}
		}
		return false;
	}

	public Object fieldGet(FieldNode key){
		String value = key.toString();
		for(FieldNode k : this.mapp.keySet()){
			if(k.toString().equals(value)){
				return mapp.get(k).read();
			}
		}
		System.out.println("No Key Exists!");
		return null;
	}

	public void fieldInsert(FieldNode key, FieldNode value){
		String valuename = key.toString();
		for(FieldNode k : this.mapp.keySet()){
			if(k.toString().equals(valuename)){
				mapp.get(k).insert(((FieldNode) value).read());
				break;
			}
		}
	}

	@Override
	public int len(){
		return this.size;
	}

	public Object read(FieldNode key){
		return fieldGet(key);
	}
	
	@Override
	public String toString(){
		StringBuilder sb = new StringBuilder("map[");
		for(FieldNode key : mapp.keySet()){
			sb.append(key.read()+":"+mapp.get(key).read()+" ");
		}
		if(sb.length() > 4){
			sb.deleteCharAt(sb.length()-1);
		}
		sb.append("]");
		return sb.toString();
	}

	public void insert(FieldNode key, FieldNode value){
		/** An issue could be that Java hashmap cannot compare (.equals()) on abstract types like key which is a fieldNode
		 *	Also when you insert the value read from the parameter fieldNode value, it might be a wrong type since we have no type checking.
		 */
		if (fieldExist(key)){
			fieldInsert(key, value);
		} else {
			this.mapp.put((FieldNode) key, (FieldNode) value);
			size++;
		}
	}
	
	public void deleteElement(FieldNode key) {
		if(fieldExist(key)){
			String value = key.toString();
			for(FieldNode k : this.mapp.keySet()){
				if(k.toString().equals(value)){
					mapp.remove(k);
					break;
				}
			}
		}
	}

	@Override
	public Object executeGeneric(VirtualFrame frame) {
		//TODO Possibly does not work properly because the fieldnodes could possibly be copied over by reference
		Map<FieldNode,FieldNode> newmap = new LinkedHashMap<>();
		for(FieldNode name : mapp.keySet()){
			newmap.put(name, mapp.get(name));
		}
		return new GoMap(keyType,valueType,newmap);
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

	@Override
	public GoPrimitiveTypes getType() {
		// TODO Need to figure out a type or something
		return null;
	}

	@Override
	public int cap() {
		//Maps have capacity but it is currently not tracked.
		return 0;
	}

	@Override
	public int lowerBound() {
		//Maps have no lowerbound
		return 0;
	}

	@Override
	public Object read(Object index) {
		//Read is covered by the FieldNode read
		return null;
	}

	@Override
	public void insert(Object index, Object writevalue) {
		FieldNode key = new FieldNode(index,null);
		FieldNode value = new FieldNode(writevalue,null);
		if (fieldExist(key)){
			fieldInsert(key, value);
		} else {
			this.mapp.put((FieldNode) key, (FieldNode) value);
			size++;
		}
	}
	
}
