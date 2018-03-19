package com.oracle.app.nodes.call;

import com.oracle.app.nodes.GoTypes;
import com.oracle.app.nodes.interop.GoForeignToGoTypeNode;
import com.oracle.app.nodes.interop.GoForeignToGoTypeNodeGen;
import com.oracle.app.runtime.GoFunction;
import com.oracle.app.runtime.GoUndefinedNameException;
import com.oracle.truffle.api.Assumption;
import com.oracle.truffle.api.RootCallTarget;
import com.oracle.truffle.api.dsl.Cached;
import com.oracle.truffle.api.dsl.Specialization;
import com.oracle.truffle.api.dsl.TypeSystemReference;
import com.oracle.truffle.api.interop.ArityException;
import com.oracle.truffle.api.interop.ForeignAccess;
import com.oracle.truffle.api.interop.Message;
import com.oracle.truffle.api.interop.TruffleObject;
import com.oracle.truffle.api.interop.UnsupportedMessageException;
import com.oracle.truffle.api.interop.UnsupportedTypeException;
import com.oracle.truffle.api.nodes.DirectCallNode;
import com.oracle.truffle.api.nodes.IndirectCallNode;
import com.oracle.truffle.api.nodes.Node;

@TypeSystemReference(GoTypes.class)
public abstract class GoGenericDispatchNode extends Node {

	public static final int INLINE_CACHE_SIZE = 2;
	//@Child private IndirectCallNode callNode = Truffle.getRuntime().createIndirectCallNode();
	
	public abstract Object executeDispatch(Object function, Object[] arguments);
	
	 @Specialization(limit = "INLINE_CACHE_SIZE", //
             		guards = "function.getCallTarget() == cachedTarget", //
             		assumptions = "callTargetStable")
	 protected static Object doDirect(GoFunction function, Object[] arguments,
             @Cached("function.getCallTargetStable()") Assumption callTargetStable,
             @Cached("function.getCallTarget()") RootCallTarget cachedTarget,
             @Cached("create(cachedTarget)") DirectCallNode callNode) {

		 return callNode.call(arguments);
	 }

	@Specialization(replaces = "doDirect")
	public Object executeDispatch(GoFunction function, Object[] arguments,
				@Cached("create()") IndirectCallNode callNode) {
		return callNode.call(function.getCallTarget(), arguments);
	}

	@Specialization(guards = "isForeignFunction(function)")
	protected static Object doForeign(TruffleObject function, Object[] arguments,
					@Cached("createCrossLanguageCallNode(arguments)") Node crossLanguageCallNode,
					@Cached("createToGoTypeNode()") GoForeignToGoTypeNode toGoTypeNode){
		try{
			Object res = ForeignAccess.sendExecute(crossLanguageCallNode, function, arguments);
			return toGoTypeNode.executeConvert(res);
		} catch(ArityException | UnsupportedTypeException | UnsupportedMessageException e){
			throw GoUndefinedNameException.undefinedFunction(function);
		}
	}
	
	protected static boolean isForeignFunction(TruffleObject function){
		return !(function instanceof GoFunction);
	}
	
	protected static Node createCrossLanguageCallNode(Object[] arguments){
		return Message.createExecute(arguments.length).createNode();
	}
	
	protected static GoForeignToGoTypeNode createToGoTypeNode(){
		return GoForeignToGoTypeNodeGen.create();
	}
}
