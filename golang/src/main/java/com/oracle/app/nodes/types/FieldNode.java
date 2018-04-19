package com.oracle.app.nodes.types;

import com.oracle.truffle.api.frame.FrameSlot;
import com.oracle.truffle.api.frame.FrameUtil;
import com.oracle.truffle.api.frame.VirtualFrame;

public class FieldNode extends GoExpressionNode{
    protected Object value;
    protected String type;

    public FieldNode(String value, Object type){
        this.value = value;
        this.type = type;
    }

    public void insert(Object value){
        this.value = value;
    }

    public Object read(){
        return this.value;
    }

}