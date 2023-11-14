import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

class ThreeAddressCodeGenerator
{
	ASTNode.ASTType typeGenerator;
	ASTNode rootNode;
	List<CodeObject> CodeObjectList = new LinkedList<CodeObject>();
	int tempCounter = 0;
	
	// Constructor obtains symbol list from table builder
	public ThreeAddressCodeGenerator(ASTNode rootNode)
	{
		this.rootNode = rootNode;
	}
	
	public List<CodeObject> GenerateThreeAdressCodeList()
	{
		GenerateCode(rootNode);
		List<ASTNode> children = rootNode.getChildren();
		
		for(ASTNode child : children)
		{
			GenerateCode(child);
		}
		
		return CodeObjectList;
	}

	public String newTemp()
	{
		tempCounter++;
		return "$T" + Integer.toString(tempCounter);
	}
	
	public void GenerateCode(ASTNode node)
	{
		ASTNode.ASTType type = node.getType();
		CodeObject.opcode objType = null;
		CodeObject objCode;
		String leftCode;
		String rightCode;
		String tempVariable;
		List<ASTNode> children;
		
		switch(type)
		{
			case assignExpr:
				children = node.getChildren();
				tempVariable = newTemp();
				String variableType = children.get(0).getVariableType();
				String assignVariable = children.get(0).getVariableName();
				type = children.get(1).getType();
				children = children.get(1).getChildren();
				String tempLeft = null;
				String tempRight = null;
				
				if(children.get(0).getType() == ASTNode.ASTType.varRef)
				{
					tempLeft = children.get(0).getVariableName();
				}if(children.get(0).getType() == ASTNode.ASTType.INTLITERAL)
				{
					CodeObjectList.add(new CodeObject(objType.STOREI, children.get(0).getValue(), tempVariable));
					tempLeft = tempVariable;
					tempVariable = newTemp();
					
				}if(children.get(0).getType() == ASTNode.ASTType.FLOATLITERAL)
				{
					CodeObjectList.add(new CodeObject(objType.STOREF, children.get(0).getValue(), tempVariable));
					tempLeft = tempVariable;
					tempVariable = newTemp();
				}
				
				if(children.get(1).getType() ==ASTNode.ASTType.varRef)
				{
					tempRight = children.get(1).getVariableName();
					
				}if(children.get(1).getType() ==ASTNode.ASTType.INTLITERAL)
				{
					CodeObjectList.add(new CodeObject(objType.STOREI, children.get(1).getValue(), tempVariable));
					tempRight = tempVariable;
					tempVariable = newTemp();
					
				}if(children.get(1).getType() ==ASTNode.ASTType.FLOATLITERAL)
				{
					CodeObjectList.add(new CodeObject(objType.STOREF, children.get(1).getValue(), tempVariable));
					tempRight = tempVariable;
					tempVariable = newTemp();
				}
				
				if(type == ASTNode.ASTType.addExpr)
				{
					if(variableType.equals("INT"))
					{
						CodeObjectList.add(new CodeObject(objType.ADDI, tempLeft, tempRight, tempVariable));
						
					}else
					{
						CodeObjectList.add(new CodeObject(objType.ADDF, tempLeft, tempRight, tempVariable));
					}
				}
				if(type == ASTNode.ASTType.subExpr)
				{
					if(variableType.equals("INT"))
					{
						CodeObjectList.add(new CodeObject(objType.SUBI, tempLeft, tempRight, tempVariable));
						
					}else
					{
						CodeObjectList.add(new CodeObject(objType.SUBF, tempLeft, tempRight, tempVariable));
					}
				}
				if(type == ASTNode.ASTType.mulExpr)
				{
					if(variableType.equals("INT"))
					{
						CodeObjectList.add(new CodeObject(objType.MULTI, tempLeft, tempRight, tempVariable));
						
					}else
					{
						CodeObjectList.add(new CodeObject(objType.MULTF, tempLeft, tempRight, tempVariable));
					}
				}
				if(type == ASTNode.ASTType.divExpr)
				{
					if(variableType.equals("INT"))
					{
						CodeObjectList.add(new CodeObject(objType.DIVI, tempLeft, tempRight, tempVariable));
						
					}else
					{
						CodeObjectList.add(new CodeObject(objType.DIVF, tempLeft, tempRight, tempVariable));
					}
				}
				
				if(variableType.equals("INT"))
				{
					CodeObjectList.add(new CodeObject(objType.STOREI, tempVariable, assignVariable));
				}else
				{
					CodeObjectList.add(new CodeObject(objType.STOREF, tempVariable, assignVariable));
				}
				break;
			case assignExprUnary:
				children = node.getChildren();
				tempVariable = newTemp();
				type = children.get(1).getType();
				if(type == ASTNode.ASTType.INTLITERAL)
				{
					CodeObjectList.add(new CodeObject(objType.STOREI, children.get(1).getValue(), tempVariable));
					CodeObjectList.add(new CodeObject(objType.STOREI, tempVariable, children.get(0).getVariableName()));
				
				}else
				{
					CodeObjectList.add(new CodeObject(objType.STOREF, children.get(1).getValue(), tempVariable));
					CodeObjectList.add(new CodeObject(objType.STOREF, tempVariable, children.get(0).getVariableName()));

				}
				break;
			case READ:
				children = node.getChildren();
				for(ASTNode child : children)
				{
					if(child.getVariableType().equals("INT"))
					{
						CodeObjectList.add(new CodeObject(objType.READI, child.getVariableName()));
					}else
					{
						CodeObjectList.add(new CodeObject(objType.READF, child.getVariableName()));
					}
				}
				break;
			case WRITE:
				children = node.getChildren();
				for(ASTNode child : children)
				{
					if(child.getType() == ASTNode.ASTType.newline)
					{
						CodeObjectList.add(new CodeObject(objType.WRITES, "newline"));
					}else
					{
						if(child.getVariableType().equals("INT"))
						{
							CodeObjectList.add(new CodeObject(objType.WRITEI, child.getVariableName()));
						}else
						{
							CodeObjectList.add(new CodeObject(objType.WRITEF, child.getVariableName()));
						}
					}
				}
				break;
		}
	}
	
	public void DislayCodeObject(ArrayList<String> instrucList)
	{
		instrucList.add(";IR code");
		instrucList.add(";LABEL main");
		instrucList.add(";LINK");
		for(CodeObject code : CodeObjectList)
		{
			CodeObject.opcode type = code.getOpcode();
			switch(type)
			{
				case ADDI:
				case SUBI:
				case MULTI:
				case DIVI:
				case ADDF:
				case SUBF:
				case MULTF:
				case DIVF:
					instrucList.add(";"+type+" "+code.getArg1()+" "+
							code.getArg2()+" "+code.getResult());
					break;
				case STOREI:
				case STOREF:
					instrucList.add(";"+type+" "+code.getArg1()+" "+code.getResult());
					break;
				case READI:
				case READF:
					instrucList.add(";"+type+" "+code.getResult());
					break;
				case WRITEI:
				case WRITEF:
				case WRITES:
					instrucList.add(";"+type+" "+code.getArg1());
					break;
			}
		}
		instrucList.add(";RET");
	}
	
}

class CodeObject
{
	enum opcode
	{
		ADDI,
		SUBI,
		MULTI,
		DIVI,
		ADDF,
		SUBF,
		MULTF,
		DIVF,
		STOREI,
		STOREF,
		READI,
		READF,
		WRITEI,
		WRITEF,
		WRITES,
	}
	
	// instance variables
	private opcode operator = null;
	private String arg1 = null;
	private String arg2 = null;
	private String result = null;
	
	//Constructors
	//Constructor for ADDI, SUBI, MULTI, DIVI, ADDF, SUBF, MULTF, DIVF
	public CodeObject(opcode operator, String arg1, String arg2, String result)
	{
		this.operator = operator;
		this.arg1 = arg1;
		this.arg2 = arg2;
		this.result = result;
	}
	
	//Constructor for STOREI, STOREF
	public CodeObject(opcode operator, String arg1, String result)
	{
		this.operator = operator;
		this.arg1 = arg1;
		this.result = result;
	}
	
	//Constructor for READI, READF, WRITEI, WRITEF, WRITES
	public CodeObject(opcode operator, String value)
	{
		if(operator == opcode.READI || operator == opcode.READF)
		{
			this.operator = operator;
			this.result = value;
		}else
		{
			this.operator = operator;
			this.arg1 = value;
		}
	}
	
	//getter
	public opcode getOpcode()
	{
		return this.operator;
	}
	
	public String getArg1()
	{
		return this.arg1;
	}
	
	public String getArg2()
	{
		return this.arg2;
	}
	
	public String getResult()
	{
		return this.result;
	}
	
}