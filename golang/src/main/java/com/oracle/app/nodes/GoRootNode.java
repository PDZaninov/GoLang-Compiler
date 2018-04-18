package com.oracle.app.nodes;

import com.oracle.app.GoLanguage;
import com.oracle.app.nodes.call.GoFieldNode;
import com.oracle.app.nodes.call.GoFuncTypeNode;
//import com.oracle.truffle.Go.builtins.GoBuiltinNode;
//import com.oracle.truffle.Go.nodes.controlflow.GoFunctionBodyNode;
import com.oracle.app.nodes.local.GoWriteLocalVariableNodeGen;
import com.oracle.app.nodes.types.GoIntNode;
import com.oracle.truffle.api.CompilerDirectives.CompilationFinal;
import com.oracle.truffle.api.Truffle;
import com.oracle.truffle.api.frame.FrameDescriptor;
import com.oracle.truffle.api.frame.FrameSlot;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.nodes.Node;
import com.oracle.truffle.api.nodes.NodeInfo;
import com.oracle.truffle.api.nodes.RootNode;
import com.oracle.truffle.api.source.SourceSection;

@NodeInfo(language = "Go", description = "The root of all Go execution trees")
public class GoRootNode extends RootNode {
    /** The function body that is executed, and specialized during execution. */
	@Child private GoIdentNode nameNode;
	@Child private GoFuncTypeNode typeNode;
    @Child private GoExpressionNode bodyNode;

    GoExpressionNode[] argumentNodes;

    /** The name of the function, for printing purposes only. */
    private final String name;

    @CompilationFinal private boolean isCloningAllowed;

    private final SourceSection sourceSection;

    
    public GoRootNode(GoLanguage language, FrameDescriptor frameDescriptor, GoIdentNode nameNode, GoFuncTypeNode typeNode, GoExpressionNode bodyNode, SourceSection sourceSection, String name) {
        super(language, frameDescriptor);
        this.nameNode = nameNode;
        this.typeNode = typeNode;
        this.bodyNode = bodyNode;
        this.name = name;
        this.sourceSection = sourceSection;
    }

    @Override
    public SourceSection getSourceSection() {
        return sourceSection;
    }

    @Override
    public Object execute(VirtualFrame frame) {
        assert getLanguage(GoLanguage.class).getContextReference().get() != null;
        if(typeNode != null) {
        	typeNode.executeGeneric(frame);
        }

        return bodyNode.executeGeneric(frame);
    }

    public void setArgumentValues(GoExpressionNode[] argumentNodes) {
        this.argumentNodes = argumentNodes;
    }

    public GoArrayExprNode getParameters() {
        return typeNode.getParams();
    }

    public GoExpressionNode getBodyNode() {
        return bodyNode;
    }

    @Override
    public String getName() {
        return name;
    }

    public void setCloningAllowed(boolean isCloningAllowed) {
        this.isCloningAllowed = isCloningAllowed;
    }

    @Override
    public boolean isCloningAllowed() {
        return isCloningAllowed;
    }

    @Override
    public String toString() {
        return "root " + name;
    }
}