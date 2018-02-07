package com.oracle.app.nodes.call;


import com.oracle.app.nodes.GoTypes;
import com.oracle.truffle.api.CallTarget;
import com.oracle.truffle.api.dsl.TypeSystemReference;
import com.oracle.truffle.api.nodes.Node;

@TypeSystemReference(GoTypes.class)
public abstract class GoDispatchNode extends Node {

    public abstract Object executeDispatch(CallTarget target, Object[] arguments);

}