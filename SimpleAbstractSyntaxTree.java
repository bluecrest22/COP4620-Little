import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

class SimpleAbstractSyntaxTree extends LittleBaseListener
{
	// Class variables
	ASTNode.ASTType typeGenerator;
	ASTNode rootNode = new ASTNode(typeGenerator.main);
	ASTNode currentNode;
	ArrayList<ASTNode> childList = new ArrayList<ASTNode>();
	ArrayList<ArrayList<Variable>> symbolTable;
	
	
	// Constructor obtains symbol list from table builder
	public SimpleAbstractSyntaxTree(ArrayList<ArrayList<Variable>> symbolTable)
	{
		this.symbolTable = symbolTable;
	}
	
	// Generates AssignExpr node
	// assign_expr : id ':=' expr ;
	@Override public void enterAssign_expr(LittleParser.Assign_exprContext ctx)
	{
		String variableName = ctx.id().getText();
		currentNode = new ASTNode(typeGenerator.assignExprUnary);
		rootNode.addChild(currentNode);
		currentNode.addChild(GenerateVarRef(variableName));
	}
	
	@Override public void exitAssign_expr(LittleParser.Assign_exprContext ctx)
	{
		for(ASTNode node : childList)
		{
			currentNode.addChild(node);
		}
		
		childList.clear();
	}
	
	@Override public void enterRead_stmt(LittleParser.Read_stmtContext ctx)
	{
		currentNode = new ASTNode(typeGenerator.READ);
		rootNode.addChild(currentNode);
		ASTNode childNode;
		
		// add FirstChildNode
		String variableName = ctx.id_list().id().getText();
		childNode = GenerateVarRef(variableName);
		currentNode.addChild(childNode);
		
		//Continue extracting further declarations
		LittleParser.Id_tailContext idTail = ctx.id_list().id_tail();
		while(idTail.getText() != "")
		{
			variableName = idTail.id().getText();
			childNode = GenerateVarRef(variableName);
			currentNode.addChild(childNode);
			idTail = idTail.id_tail();
		}
		
		System.out.println();
		
	}
	
	@Override public void enterWrite_stmt(LittleParser.Write_stmtContext ctx)
	{
		currentNode = new ASTNode(typeGenerator.WRITE);
		rootNode.addChild(currentNode);
		ASTNode childNode;
		
		// add FirstChildNode
		String variableName = ctx.id_list().id().getText();
		if(variableName.equals("newline"))
		{
			childNode = new ASTNode(typeGenerator.newline);
			
		}else
		{
			childNode = GenerateVarRef(variableName);
		}
		
		currentNode.addChild(childNode);
		
		//Continue extracting further declarations
		LittleParser.Id_tailContext idTail = ctx.id_list().id_tail();
		while(idTail.getText() != "")
		{
			variableName = idTail.id().getText();
			if(variableName.equals("newline"))
			{
				childNode = new ASTNode(typeGenerator.newline);
			}else
			{
				childNode = GenerateVarRef(variableName);
			}
			
			currentNode.addChild(childNode);
			idTail = idTail.id_tail();
		}
	}
	
	@Override public void enterAddop(LittleParser.AddopContext ctx)
	{
		String operatorType = ctx.getText();
		ASTNode opNode;
		
		if(operatorType.equals("+"))
		{
			opNode = new ASTNode(typeGenerator.addExpr);
		}
		else
		{
			opNode = new ASTNode(typeGenerator.subExpr);
		}
		
		currentNode.setType(typeGenerator.assignExpr);
		currentNode.addChild(opNode);
		currentNode = opNode;
		
		
	}
	
	@Override public void enterMulop(LittleParser.MulopContext ctx)
	{
		String operatorType = ctx.getText();
		ASTNode opNode;
		
		if(operatorType.equals("*"))
		{
			opNode = new ASTNode(typeGenerator.mulExpr);
		}
		else
		{
			opNode = new ASTNode(typeGenerator.divExpr);
		}
		
		currentNode.setType(typeGenerator.assignExpr);
		currentNode.addChild(opNode);
		currentNode = opNode;
	}
	
	@Override public void enterPrimary(LittleParser.PrimaryContext ctx)
	{
		String primaryType = ctx.getText();
		if(primaryType.contains("(")){return;}
		if(isNumeric(primaryType))
		{
			if(primaryType.contains("."))
			{
				float floatValue = Float.parseFloat(primaryType);
				ASTNode floatNode = new ASTNode(typeGenerator.FLOATLITERAL, floatValue);
				childList.add(floatNode);
			}else
			{
				int intValue = Integer.parseInt(primaryType);
				ASTNode intNode = new ASTNode(typeGenerator.INTLITERAL, intValue);
				childList.add(intNode);
			}
			
		}else
		{
			childList.add(GenerateVarRef(primaryType));
		}
	}
	
	
	public Variable SearchSymbolTable(String variableID)
	{
		ArrayList<Variable> block = symbolTable.get(0);
		Variable returnVariable = new Variable(variableID);
		for(Variable variable : block)
		{
			if(variable.getName().equals(variableID))
			{
				returnVariable = variable;
			}
		}
		return returnVariable;
	}
	
	public boolean isNumeric(String str) 
	{ 
		  try 
		  {
		    Float.parseFloat(str);
		    return true;
		  } catch(NumberFormatException e)
		  {  
		    return false;  
		  }  
	}
	
	public ASTNode GenerateVarRef(String variableName)
	{
		String variableID = variableName;
		Variable variable = SearchSymbolTable(variableID);
		String variableType = variable.getType();
		return new ASTNode(typeGenerator.varRef, variableName, variableType);
	}
	
	public ASTNode getRoot()
	{
		return rootNode;
	}
	
}

class ASTNode
{
	enum ASTType 
	{
		main,
		assignExpr,
		assignExprUnary,
		varRef,
		INTLITERAL,
		FLOATLITERAL,
		READ,
		WRITE,
		mulExpr,
		addExpr,
		subExpr,
		divExpr,
		newline,
	}
	private ASTType type;
	private String variableName;
	private String variableType;
	private String value;
	private List<ASTNode> childNodes = new LinkedList<ASTNode>();
	
	// Constructor for either VarRef, READ, WRITE, Mul
	public ASTNode(ASTType type)
	{
		this.type = type;
	}
	
	// Constructor for VarRef
	public ASTNode(ASTType type, String variableName, String variableType)
	{
		this.type = type;
		this.variableName = variableName;
		this.variableType = variableType;
		
	}
	// Constructor for INTLITERAL
	public ASTNode(ASTType type, int intValue)
	{
		this.type = type;
		this.value = Integer.toString(intValue);
	}
	// Constructor for FLOATLITERAL
	public ASTNode(ASTType type, float floatValue)
	{
		this.type = type;
		this.value = Float.toString(floatValue);
	}
	
	// public void addChild
	public void addChild(ASTNode childNode)
	{
		this.childNodes.add(childNode);
	}

	// Gets the child nodes of a node
	public List<ASTNode> getChildren()
	{
		return childNodes;
	}
	
	//Getter gets Node type
	public ASTType getType()
	{
		return this.type;
	}
	
	//Setter changes the type of Node type
	public void setType(ASTType type)
	{
		this.type = type;
	}
	
	//Getter for variableName
	public String getVariableName()
	{
		return this.variableName;
	}
	
	//Getter for value
	public String getValue()
	{
		return this.value;
	}
	
	//Getter for variableType
	public String getVariableType()
	{
		return variableType;
	}
}