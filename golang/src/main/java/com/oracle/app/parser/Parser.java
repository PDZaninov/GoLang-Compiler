package com.oracle.app.parser;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.oracle.app.GoException;
import com.oracle.app.GoLanguage;
import com.oracle.app.nodes.GoFileNode;
import com.oracle.app.nodes.GoRootNode;
import com.oracle.app.parser.ir.GoBaseIRNode;
import com.oracle.app.parser.ir.GoTruffle;
import com.oracle.app.parser.ir.nodes.*;
import com.oracle.truffle.api.source.Source;

/**
 * 
 * Parses an ast file generated by the GoLang and creates
 * a 1-1 mapping of Go source code into our own IR representation.
 *
 */
public class Parser {
	//Magic number eraser
	private final int stringAttr  = 1;
	private final int stringVal   = 2;
	private final int regularAttr = 3;
	private final int regularVal  = 4;
	
	private final String file; // the file we open
	private GoLanguage language; // language we are passed
	private Source source;
	private BufferedReader reader; // used to read file
	private String currentLine; // String of the current line we are on
	private Matcher matchedTerm; // used for regex/parsing of file
	private Pattern astPattern = Pattern.compile("\\[\\]\\*ast\\.\\w+|\\.\\w+"); //for getting the type of node
	private Pattern nodeTypePattern = Pattern.compile("\\w+:|\"\\w+\"");
	private Pattern attrPattern= Pattern.compile("(\\w+): \"(.+)\"|(\\w+): (.+)"); //for getting the attributes
	

	/**
	 * 
	 * @param language
	 * @param source
	 * @throws FileNotFoundException
	 */
	public Parser(GoLanguage language, Source source) throws FileNotFoundException {
		this.file = source.getName().substring(0, source.getName().lastIndexOf('.')) + ".ast";;
		this.language = language;
		this.source = source;
		reader = new BufferedReader(new FileReader(this.file));
	}

	
	/**
	 * The starting point for the parse function. Initiates the call
	 * for the { Go Truffle.class} visitor.
	 * @return A Hashmap containing all function definitions
	 * @throws IOException
	 */
	public GoFileNode beginParse() throws IOException{
		String type;
		GoBaseIRNode k = null;
		while((currentLine = reader.readLine()) != null){
			matchedTerm = astPattern.matcher(currentLine);
			if(matchedTerm.find()){
			
				type = matchedTerm.group().substring(1);
				k = recParse(type);
			}
		}
		
		//Tree visitor for printing out the tree
		//GoVisitor visitor = new GoVisitor();
		//k.accept(visitor);
		
		GoTruffle truffleVisitor = new GoTruffle(language, source);
		truffleVisitor.initialize();
		GoFileNode result = (GoFileNode) k.accept(truffleVisitor);
		if(truffleVisitor.checkForCompileErrors()){
			throw new GoException("");
		}
		return result;
	}
	
	/**
	 * Depth first traversal of the ast file to recursively create the IRTree from the ast file. 
	 * Every occurence of an opening brace indicates a new node in the GoLang, so call recParse on the new node
	 * Every occurence of a closing brace indicates the ending of the current node so call getIRNode to generate the IRNode
	 * If no braces occur then fill in the attribute Map for  the current node
	 * @param currNode The current node of interest
	 * @return The new IRNode after gathering all the type information on it
	 * @throws IOException
	 */
	private GoBaseIRNode recParse(String currNode) throws IOException {
		Map<String, GoBaseIRNode> body = new LinkedHashMap<>();
		Map<String, String> attrs = new HashMap<>();
		String nodeName = currNode;
		
		//while statement reading the file
		
		while((currentLine = reader.readLine()) != null) {
			if(currentLine.indexOf('}') != -1) {
				
	    		//creating itself, going up
				return getIRNode(nodeName,attrs,body);

	    	}
	    	else if(currentLine.indexOf('{') >= 0) {
	    		//going deeper into the tree creating children first
	    		matchedTerm = astPattern.matcher(currentLine);
	    		if(matchedTerm.find()){
	    			String nodeType = matchedTerm.group().substring(1);
	    			matchedTerm = nodeTypePattern.matcher(currentLine);
	    			matchedTerm.find();
	    			String type = matchedTerm.group();
	    			type = type.substring(0,type.length()-1);
	    			body.put(type, recParse(nodeType));
	    			
	    		}
	    	}
	    	else {
	    		//adding attributes
	    		matchedTerm = attrPattern.matcher(currentLine);
	    		if(matchedTerm.find()){
	    			if(matchedTerm.group(stringAttr) == null){
	    				attrs.put(matchedTerm.group(regularAttr), matchedTerm.group(regularVal));
	    			}
	    			else{
	    				attrs.put(matchedTerm.group(stringAttr), matchedTerm.group(stringVal));
	    			}
	    		}
	    	}
		}
		
		return null;
	}
	
	public ArrayList<GoBaseIRNode> packIntoArrayList(Collection<GoBaseIRNode> body){
		ArrayList<GoBaseIRNode> list = new ArrayList<>();
		for(GoBaseIRNode child : body){
			list.add(child);
		}
		return list;
	}
	
	/**
	 * Create the IRNode.
	 * Match the nodetype to a switch case to create the specific IRNode that corresponds to
	 * the name and pass in the necessary attributes and children nodes in the constructor.
	 * @param nodeType The name of the node to make
	 * @param attrs The attributes associated with the node
	 * @param body The body nodes to insert for the new node
	 * @return The new IRNode
	 */
	public GoBaseIRNode getIRNode(String nodeType, Map<String,String> attrs, Map<String,GoBaseIRNode> body) {
		switch(nodeType) {
			case "ArrayType":
				return new GoIRArrayTypeNode(body.get("Len"),body.get("Elt"),attrs.get("Lbrack"));
				
			case "AssignStmt":
				GoIRArrayListExprNode lhs = (GoIRArrayListExprNode) body.get("Lhs");
				GoIRArrayListExprNode rhs = (GoIRArrayListExprNode) body.get("Rhs");
				String assigntype = attrs.get("Tok");
				switch(assigntype){
				case "=":
				case ":=":
					//Might need to distinguish between = and :=. 
					//= is only for defined variables
					//:= can be used for defined or undefined variables
					return createAssignment(lhs,rhs,attrs.get("TokPos"));
				case "+=":
				case "-=":
				case "*=":
				case "/=":
				case "%=":
					assigntype = assigntype.substring(0,1);
					return assignNormalize(assigntype,lhs,rhs,attrs.get("TokPos"));
				
				default:
					System.out.println("Error unknown assignment: " + assigntype);
				}
				return new GoTempIRNode(nodeType,attrs,body);
				
			case "BasicLit":
				return GoIRBasicLitNode.createBasicLit(attrs.get("Kind"),
						attrs.get("Value"),
						attrs.get("ValuePos")
						);
				
			case "BinaryExpr":
				return new GoIRBinaryExprNode(attrs.get("Op"),
						body.get("X"),
						body.get("Y"),
						attrs.get("OpPos")
						);
				
			case "BranchStmt":
				return new GoIRBranchStmtNode(attrs.get("Tok"),
						body.get("Label"),
						attrs.get("TokPos")
						);
				
			case "UnaryExpr":
				return new GoIRUnaryNode(attrs.get("Op"),body.get("X"),attrs.get("OpPos"));
				
			case "BlockStmt":
				return new GoIRBlockStmtNode((GoIRStmtNode) body.get("List"),
						attrs.get("Lbrace"),
						attrs.get("Rbrace")
						);
				
			case "CallExpr":
				GoBaseIRNode functionNode = body.get("Fun");
				GoIRArrayListExprNode args = (GoIRArrayListExprNode) body.get("Args");
				return new GoIRInvokeNode(functionNode,
						args,
						attrs.get("Lparen"),
						attrs.get("Ellipsis"),
						attrs.get("Rparen")
						);

			case "CompositeLit":
				GoBaseIRNode expr = body.get("Type");
				String lbrace = attrs.get("Lbrace");
				GoIRArrayListExprNode elts = (GoIRArrayListExprNode) body.get("Elts");
				String rbrace = attrs.get("Rbrace");
				return new GoIRCompositeLitNode(expr,lbrace,elts,rbrace);
				
			case "CaseClause":
				return new GoIRCaseClauseNode((GoIRArrayListExprNode) body.get("List"),
						(GoIRStmtNode) body.get("Body"),
						attrs.get("Case"),
						attrs.get("Colon")
						);
				
			case "DeclStmt":
				return new GoIRDeclStmtNode(body.get("Decl"));
				
			case "Decl":
				return new GoIRArrayListExprNode(packIntoArrayList(body.values()));
				
			case "Expr":
				return new GoIRArrayListExprNode(packIntoArrayList(body.values()));
				
			case "ExprStmt":
				return new GoIRExprStmtNode(body.get("X"));
				
			case "Field":
				if(body.size()==2) {
					return new GoIRFieldNode(nodeType,((GoIRArrayListExprNode)body.get("Names")), body.get("Type"));
				}
				else {
					return new GoIRFieldNode(nodeType, body.get("Type"));
				}
				
			case "]*ast.Field":
				return new GoIRArrayListExprNode(packIntoArrayList(body.values()));
		
			case "FieldList":
				return new GoIRFieldListNode((GoIRArrayListExprNode) body.get("List"));
				
			case "File":
				return new GoIRFileNode((GoIRIdentNode) body.get("Name"),body.get("Decls"),body.get("Imports"));
				
			case "ForStmt":
				GoBaseIRNode init = body.get("Init");
				GoBaseIRNode cond = body.get("Cond");
				GoBaseIRNode post = body.get("Post");
				GoBaseIRNode for_body = body.get("Body");
				return new GoIRForNode(init,
						cond,
						post,
						for_body,
						attrs.get("For"));
				
			case "FuncDecl"://(GoBaseIRNode receiver, GoBaseIRNode name, GoBaseIRNode type, GoBaseIRNode body)
				GoBaseIRNode recv = body.get("Recv");
				GoBaseIRNode name = body.get("Name");
				GoBaseIRNode type = body.get("Type");
				GoBaseIRNode func_body = body.get("Body");
				if(type != null&& name != null) {
					GoTruffle.IRFunctions.put(name.getIdentifier(), (GoIRFuncTypeNode)type);
				}
				return new GoIRFuncDeclNode(recv,
						name,
						type,
						func_body);
				
			case "FuncType":
				return new GoIRFuncTypeNode(body.get("Params"), body.get("Results"),attrs.get("Func"));
				
			case "GenDecl":
				return new GoIRGenDeclNode(attrs.get("Tok"),
						(GoIRArrayListExprNode) body.get("Specs"),
						attrs.get("TokPos"),attrs.get("Lparen"),
						attrs.get("Rparen")
						);
				
			case "]*ast.Ident":
				return new GoIRArrayListExprNode(packIntoArrayList(body.values()));
				
			case "Ident":
				GoBaseIRNode obj = body.get("Obj");
				return new GoIRIdentNode(attrs.get("Name"),
						obj,
						attrs.get("NamePos")
						);
			
			case "IfStmt":
				GoBaseIRNode ifinit = body.get("Init");
				GoBaseIRNode ifcond = body.get("Cond");
				GoBaseIRNode ifblock = body.get("Body");
				GoBaseIRNode elseblock = body.get("Else");
				return new GoIRIfStmtNode(ifinit,
						ifcond,
						ifblock,
						elseblock,
						attrs.get("If")
						);
				
			case "IndexExpr":
				return new GoIRIndexNode((GoBaseIRNode) body.get("X"),
						body.get("Index"),
						attrs.get("Lbrack"),
						attrs.get("Rbrack")
						);
			case "]*ast.ImportSpec":
				return new GoTempIRNode(nodeType,attrs,body);
				
			case "ImportSpec":
				return new GoIRImportSpecNode((GoIRBasicLitNode) body.get("Path"));

			case "IncDecStmt":
				return new GoIRIncDecStmtNode(attrs.get("Tok"),
						body.get("X"),
						attrs.get("TokPos")
						);
			case "KeyValueExpr":
				return new GoIRKeyValueNode(body.get("Key"),attrs.get("Colon"),body.get("Value"));
			case "MapType":
				return new GoIRMapTypeNode(body.get("Key"),body.get("Value"));
			case "Object":
				return new GoIRObjectNode(body.get("Decl"), attrs.get("Kind"));
				
			case "ParenExpr":
				return new GoIRExprStmtNode(body.get("X"),
						attrs.get("Lparen"),
						attrs.get("Rparen")
						);
			case "RangeStmt":
				return null;
			case "ReturnStmt":
				return new GoIRReturnStmtNode((GoIRArrayListExprNode)body.get("Results"),attrs.get("Return"));
				
			case "Scope":
				return new GoTempIRNode(nodeType,attrs,body);
				
			case "SelectorExpr":
				return new GoIRSelectorExprNode(body.get("X"),(GoIRIdentNode) body.get("Sel"));
				
			case "SliceExpr":
				GoBaseIRNode sliceexpr = body.get("X");
				String slbrack = attrs.get("Lbrack");
				GoBaseIRNode low = body.get("Low");
				GoBaseIRNode high = body.get("High");
				GoBaseIRNode max = body.get("Max");
				String slice3 = attrs.get("Slice3");
				String srbrack = attrs.get("Rbrack");
				return new GoIRSliceExprNode(sliceexpr,slbrack,low,high,max,slice3,srbrack);
				
			case "Spec":
				ArrayList<GoBaseIRNode> speclist = new ArrayList<>();
				for(GoBaseIRNode child : body.values()){
					speclist.add(child);
				}
				
				return new GoIRArrayListExprNode(speclist);
			
			case "StarExpr":
				return new GoIRStarNode(body.get("X"), attrs.get("Star"));
				
			case "Stmt":
				return new GoIRStmtNode(packIntoArrayList(body.values()));

			case "StructType":
				return new GoIRStructTypeNode((GoIRFieldListNode) body.get("Fields"),attrs.get("Incomplete"));

			case "SwitchStmt":
				GoIRStmtNode switchinit = (GoIRStmtNode) body.get("Init");
				GoBaseIRNode tag = body.get("Tag");
				GoIRBlockStmtNode switchbody = (GoIRBlockStmtNode) body.get("Body");
				return new GoIRSwitchStmtNode(switchinit,
						tag,
						switchbody,
						attrs.get("Switch")
						);

			case "TypeSpec":
				return new GoIRTypeSpecNode(body.get("Name"), body.get("Type"));
				
			case "ValueSpec":
				GoIRArrayListExprNode names = (GoIRArrayListExprNode) body.get("Names");
				GoBaseIRNode valuetype = body.get("Type");
				GoIRArrayListExprNode values = (GoIRArrayListExprNode) body.get("Values");
				if(values == null){
					return createAssignment(names, valuetype, attrs.get("TokPos"));
				}
				else{
					return createAssignment(names,valuetype,values,attrs.get("TokPos"));
				}
			//Idk if comments are supposed to do anything technically so they are just null
			case "Comment":
				return null;
			case "]*ast.Comment":
				return null;
			case "CommentGroup":
				return null;
			case "]*ast.CommentGroup":
				return null;
			default:
				System.out.println("Error, in default: " + nodeType);
				
		}
		return new GoTempIRNode(nodeType,attrs,body);
	}

	/*
	 * Should throw an error when either side have unbalanced arrays or if either is empty
	 * 
	 */
	public GoIRArrayListExprNode createAssignment(GoIRArrayListExprNode lhs, GoIRArrayListExprNode rhs, String source){
		int size = lhs.getSize();
		GoBaseIRNode writeto;
		ArrayList<GoBaseIRNode> result = new ArrayList<>();
		
		if(size != rhs.getSize()){
			
			if((rhs.getChildren().get(0)) instanceof GoIRInvokeNode)
			{
			}
			else {
				throw new GoException("Parse error uneven assignment");
			}
		}
		

		for(int i = 0; i < size;i++){
			if(lhs.getChildren().get(i) instanceof GoIRIdentNode) {
				((GoIRIdentNode) lhs.getChildren().get(i)).setPos(i);
			}
			if((rhs.getChildren().get(0)) instanceof GoIRInvokeNode)
			{
				((GoIRInvokeNode)rhs.getChildren().get(0)).incAssignLen();
				writeto = lhs.getChildren().get(i);
				result.add(new GoIRAssignmentStmtNode(writeto,rhs.getChildren().get(0) ,null,source));
				continue;
				
			}
			
			writeto = lhs.getChildren().get(i);
			result.add(new GoIRAssignmentStmtNode(writeto,rhs.getChildren().get(i),null,source));
		}
		return new GoIRArrayListExprNode(result, source);
	}
	
	/*
	 * Given no right hand side, set default values of the type to each ident
	 * Called by valuespec
	 * TODO this function is broken for arraytype types.
	 */
	public GoIRArrayListExprNode createAssignment(GoIRArrayListExprNode lhs, GoBaseIRNode type, String source){
		ArrayList<GoBaseIRNode> result = new ArrayList<>();
		for(GoBaseIRNode node : lhs.getChildren()){
			result.add(new GoIRAssignmentStmtNode(node,type,type,source));
		}
		return new GoIRArrayListExprNode(result, source);
	}
	
	/*
	 * Given a type and a right hand side, the right hand side should match the type given
	 * Assuming that the type matches the right hand side always for now
	 * Called by valuespec
	 * var x = vals()
	 */
	public GoIRArrayListExprNode createAssignment(GoIRArrayListExprNode lhs, GoBaseIRNode type, GoIRArrayListExprNode rhs, String source){

		ArrayList<GoBaseIRNode> result = new ArrayList<>();
		int size = lhs.getSize();
		
		if(rhs.getChildren().get(0) instanceof GoIRInvokeNode)
			{
				for(int i = 0; i < size;i++){
					((GoIRIdentNode) lhs.getChildren().get(i)).setPos(i);
					((GoIRInvokeNode)rhs.getChildren().get(0)).incAssignLen();
					result.add(new GoIRAssignmentStmtNode(lhs.getChildren().get(i),rhs.getChildren().get(0), type,source));
				}
				return new GoIRArrayListExprNode(result, source);
			}

		//if rhs not invoke node and sides arent equal throw error
		if(lhs.getSize() != rhs.getSize())
		{
			throw new GoException("Uneven sides");
		}

		for(int i = 0; i < size;i++) {
			// setting the number of returns expected. This is for checking the number of variables = number of returns
			((GoIRIdentNode) lhs.getChildren().get(i)).setPos(i);
			
			
			if(type==null) {
				result.add(new GoIRAssignmentStmtNode(lhs.getChildren().get(i),rhs.getChildren().get(i),null,source));
			}
			else if(type.getIdentifier().equalsIgnoreCase(((GoIRBasicLitNode) (rhs.getChildren().get(i))).getType()))
				{
				result.add(new GoIRAssignmentStmtNode(lhs.getChildren().get(i),rhs.getChildren().get(i) , type,source));
			}
			else if(((GoIRBasicLitNode) rhs.getChildren().get(i)).getType().equalsIgnoreCase("INT") &&
					(type.getIdentifier().equals("float32")||type.getIdentifier().equals("float64"))){
				// var c float32 = 3 -- example of this case
				// the above sets the basic lit as an int node, so I need to make a new basiclit node of the correct type
				//TODO Errors out when a float64 var is assigned a float value
				GoIRBasicLitNode m = GoIRBasicLitNode.createBasicLit(type.getIdentifier(),((GoIRBasicLitNode) rhs.getChildren().get(i)).getValString(), "");
				result.add(new GoIRAssignmentStmtNode(lhs.getChildren().get(i), m ,type,source));

			}
			else {
				throw new GoException("cannot use \"" + ((GoIRBasicLitNode) rhs.getChildren().get(i)).getValString() +
						"\" (type " + ((GoIRBasicLitNode) rhs.getChildren().get(i)).getType() +") as type " 
						+ type.getIdentifier() + " in assignment");
			}
			
			
		}
		return new GoIRArrayListExprNode(result, source);
	}
	
	/**
	 * Assignment operators assumed to only have one child in lhs and rhs
	 * else there is an error
	 * @param op
	 * @param l
	 * @param r
	 * @return
	 */
	public GoIRArrayListExprNode assignNormalize(String op,GoIRArrayListExprNode l, GoIRArrayListExprNode r, String source){
		//for type checking
		((GoIRIdentNode) l.getChildren().get(0)).setPos(0);
		if(r.getChildren().get(0) instanceof GoIRInvokeNode) {
			((GoIRInvokeNode)r.getChildren().get(0)).incAssignLen();
		}
		//creating assignment
		GoBaseIRNode temp = new GoIRBinaryExprNode(op,l.getChildren().get(0),r.getChildren().get(0),null);
		r.getChildren().set(0, temp);
		return createAssignment(l,r,source);
	}
	
	/**
	 * The starting point of the parse function called from GoLanguage
	 * @param language
	 * @param source
	 * @return
	 * @throws IOException
	 */
	public static GoFileNode parseGo(GoLanguage language, Source source) throws IOException{
		Parser parser = new Parser(language, source);
		return parser.beginParse();
	}
	
}