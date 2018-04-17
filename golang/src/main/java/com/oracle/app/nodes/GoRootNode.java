package com.oracle.app.nodes;

import com.oracle.app.GoLanguage;
import com.oracle.app.nodes.call.GoFieldNode;
import com.oracle.app.nodes.call.GoFuncTypeNode;
//import com.oracle.truffle.Go.builtins.GoBuiltinNode;
//import com.oracle.truffle.Go.nodes.controlflow.GoFunctionBodyNode;
import com.oracle.app.nodes.local.GoWriteLocalVariableNodeGen;
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

    private FrameDescriptor frameDescriptor;
    
    public GoRootNode(GoLanguage language, FrameDescriptor frameDescriptor, GoIdentNode nameNode, GoFuncTypeNode typeNode, GoExpressionNode bodyNode, SourceSection sourceSection, String name) {
        super(language, frameDescriptor);
        this.nameNode = nameNode;
        this.typeNode = typeNode;
        this.bodyNode = bodyNode;
        this.name = name;
        this.sourceSection = sourceSection;
        this.frameDescriptor = frameDescriptor;
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
        	assignToSlot(frame);
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

    public void assignToSlot(VirtualFrame frame) {
        if(this.getParameters().getArguments().length == 0) {
            return;
        }
        GoExpressionNode[] params = ((GoArrayExprNode) this.getParameters().getArguments()[0]).getArguments();

        if(params.length != argumentNodes.length) {
            throw new RuntimeException("Parameter mismatch: " + this.toString());
        }

        GoIdentNode child;
        for(int i = 0; i < argumentNodes.length; i++) {
            child = ((GoFieldNode) params[i]).getIdentifier();
            child.setChild(argumentNodes[i]);
            writeValue(frame, child.getName(), argumentNodes[i]);
        }
    }

    public void writeValue(VirtualFrame frame, String name , GoExpressionNode value) {
        FrameSlot slot = frameDescriptor.findOrAddFrameSlot(name);
        GoExpressionNode write = GoWriteLocalVariableNodeGen.create(value, slot);
        write.executeGeneric(frame);
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