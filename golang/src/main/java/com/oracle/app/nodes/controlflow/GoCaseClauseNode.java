package com.oracle.app.nodes.controlflow;

import com.oracle.app.nodes.GoExpressionNode;
import com.oracle.app.nodes.GoStatementNode;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.NodeInfo;

@NodeInfo(shortName = "case clause", description = "A node implementing a single case in a switch statement")
public final class GoCaseClauseNode extends GoStatementNode {

    @Children private final GoExpressionNode[] list;
    @Children private final GoStatementNode[]  body;
    public String caseType = "case";

    public GoCaseClauseNode(GoExpressionNode[] list, GoStatementNode[] body) {
        this.list = list;
        this.body = body;
        if(list == null){
            caseType = "default";
        }
    }

    /**
     * It checks all expressions in this case (could be multiple for a single case).
     * If match, execute its statement nodes then return true so BlockNode can break.
     * Else return false so BlockNode can execute the next case.
     *
     * @param frame: Virtual frame
     * @param value: Value of the tag passed from SwitchStatement and the block statement.
     */
    public boolean caseExecute(VirtualFrame frame, Object value){
        if(caseType == "default"){
            for (GoStatementNode executeBody : body){
                executeBody.executeVoid(frame);
            }
        } else {
            for (GoExpressionNode caseListElem : list) {
                if (caseListElem.executeGeneric(frame) == value) {
                    for (GoStatementNode executeBody : body) {
                        executeBody.executeVoid(frame);
                    }
                    return true;
                }
            }
        }
        return false;
    }

	@Override
	public void executeVoid(VirtualFrame frame) {
		for(GoStatementNode node : body){
			node.executeVoid(frame);
		}
		
	}
}

