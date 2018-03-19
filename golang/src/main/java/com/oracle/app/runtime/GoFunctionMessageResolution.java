package com.oracle.app.runtime;

import static com.oracle.app.runtime.GoContext.fromForeignValue;

import com.oracle.app.nodes.call.GoGenericDispatchNode;
import com.oracle.app.nodes.call.GoGenericDispatchNodeGen;
import com.oracle.app.nodes.interop.GoTypeToForeignNode;
import com.oracle.app.nodes.interop.GoTypeToForeignNodeGen;
import com.oracle.truffle.api.interop.CanResolve;
import com.oracle.truffle.api.interop.MessageResolution;
import com.oracle.truffle.api.interop.Resolve;
import com.oracle.truffle.api.interop.TruffleObject;
import com.oracle.truffle.api.nodes.Node;

@MessageResolution(receiverType = GoFunction.class)
public class GoFunctionMessageResolution {

	@Resolve(message = "EXECUTE")
	public abstract static class GoForeignFunctionExecuteNode extends Node{
		
		@Child private GoGenericDispatchNode dispatch = GoGenericDispatchNodeGen.create();
		@Child private GoTypeToForeignNode toForeign = GoTypeToForeignNodeGen.create();
		
		public Object access(GoFunction receiver, Object[] arguments){
			Object[] arr = new Object[arguments.length];
			
			for(int i = 0; i < arr.length; i++){
				arr[i] = fromForeignValue(arguments[i]);
			}
			Object result = dispatch.executeDispatch(receiver, arr);
			return toForeign.executeConvert(result);
		}
	}
	
	@Resolve(message = "IS_EXECUTABLE")
    public abstract static class GoForeignIsExecutableNode extends Node {
        public Object access(Object receiver) {
            return receiver instanceof GoFunction;
        }
    }
	
	@CanResolve
	public abstract static class CheckFunction extends Node{
		protected static boolean test(TruffleObject receiver){
			return receiver instanceof GoFunction;
		}
	}
	
}
