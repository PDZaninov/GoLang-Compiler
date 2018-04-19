package com.oracle.app.nodes.types;

import com.oracle.truffle.api.frame.FrameSlot;
import com.oracle.truffle.api.frame.FrameUtil;
import com.oracle.truffle.api.frame.VirtualFrame;
import java.util

public class GoStruct extends GoNonPrimitiveType{
    protected LinkedHashMap<String, FieldNode> symbolTable;
    protected boolean incomplete;

    public GoStruct(){
        this.symbolTable = new LinkedHashMap<>();
        if(symbol-table.isEmpty()){
            incomplete = true;
        } else { incomplete = false; }
    }

    public Object read(String key){
        return this.symbolTable.get(key).read();
    }

    public void write(String key, Object value){
        this.symbolTable.get(key).insert(value);
    }

    public void insertField(String key, FieldNode node){
        this.symbolTable.put(key, node);
    }

    public Object executeGeneric(VirtualFrame frame){
        return this;
    }
}