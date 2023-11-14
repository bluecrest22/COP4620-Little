import java.util.ArrayList;
import java.util.Stack;

class SimpleTableBuilder extends LittleBaseListener
{
	// Class Attributes
	Stack<ArrayList<Variable>> scopeStack = new Stack<ArrayList<Variable>>();
	ArrayList<ArrayList<Variable>> symbolTable = new ArrayList<ArrayList<Variable>>();
	int blockNumber = 0;
	
	// Getter function
	// Returns symbol table
	public ArrayList<ArrayList<Variable>> getSymbolTable()
	{
		return symbolTable;
	}
	
	@Override public void enterProgram(LittleParser.ProgramContext ctx)
	{
		//1. Make a new symbol table for "Global"
		ArrayList<Variable> global = new ArrayList<Variable>();
		global.add(new Variable("GLOBAL"));
		
		//2. add it to the list of symbol tables
		symbolTable.add(global);
		
		//3. push it to the "scope stack"
		scopeStack.push(global);
	}
	
	@Override public void enterFunc_decl(LittleParser.Func_declContext ctx)
	{
		ArrayList<Variable> function = new ArrayList<Variable>();
		function.add(new Variable(ctx.id().getText()));
		
		//2. add it to the list of symbol tables
		symbolTable.add(function);
		
		//3. push it to the "scope stack"
		scopeStack.push(function);

	}
	
	@Override public void exitFunc_decl(LittleParser.Func_declContext ctx)
	{
		// pop scope from stack
		scopeStack.pop();
	}
	
	@Override public void enterIf_stmt(LittleParser.If_stmtContext ctx) 
	{
		if(ctx.else_part().getText() != "")
		{
			ArrayList<Variable> block = new ArrayList<Variable>();
			blockNumber++;
			block.add(new Variable("BLOCK " + Integer.toString(blockNumber)));
			
			//2. add it to the list of symbol tables
			symbolTable.add(block);
			
			//3. push it to the "scope stack"
			scopeStack.push(block);
			
		}
		ArrayList<Variable> block = new ArrayList<Variable>();
		blockNumber++;
		block.add(new Variable("BLOCK " + Integer.toString(blockNumber)));
		
		//2. add it to the list of symbol tables
		symbolTable.add(block);
		
		//3. push it to the "scope stack"
		scopeStack.push(block);
	}
	
	@Override public void exitIf_stmt(LittleParser.If_stmtContext ctx) 
	{ 
		// pop scope from stack
		scopeStack.pop();
	}
	
	@Override public void exitElse_part(LittleParser.Else_partContext ctx)
	{
		// pop scope from stack
		scopeStack.pop();
	}
	
	@Override public void enterWhile_stmt(LittleParser.While_stmtContext ctx)
	{
		ArrayList<Variable> block = new ArrayList<Variable>();
		blockNumber++;
		block.add(new Variable("BLOCK " + Integer.toString(blockNumber)));
		
		//2. add it to the list of symbol tables
		symbolTable.add(block);
		
		//3. push it to the "scope stack"
		scopeStack.push(block);
	}
	
	@Override public void exitWhile_stmt(LittleParser.While_stmtContext ctx)
	{
		// pop scope from stack
		scopeStack.pop();
	}
	
	@Override public void enterString_decl(LittleParser.String_declContext ctx)
	{
		//1. extract the name, type, value
		String name = ctx.id().getText();
		String type = "STRING";
		String value = ctx.str().getText();
		//2. create a new table entry using above info and insert to the
		// table atop of the stack
		Variable declaration = new Variable(name, type, value);
		if(duplicateDeclaration(declaration))
		{
			System.exit(0);
		}
	}
	
	@Override public void enterVar_decl(LittleParser.Var_declContext ctx)
	{
		//1 extract the name, type
		String name = ctx.id_list().id().getText();
		String type = ctx.var_type().getText();
		Variable declaration = new Variable(name, type);
		if(duplicateDeclaration(declaration))
		{
			System.exit(0);
		}
		
		//Continue extracting further declarations
		LittleParser.Id_tailContext idTail = ctx.id_list().id_tail();
		while(idTail.getText() != "")
		{
			name = idTail.id().getText();
			declaration = new Variable(name, type);
			if(duplicateDeclaration(declaration))
			{
				System.exit(0);
			}
			idTail = idTail.id_tail();
		}
	}
	@Override public void enterParam_decl_list(LittleParser.Param_decl_listContext ctx)
	{
		if(ctx.getText() == "")
		{
			return;
		}
		String name = ctx.param_decl().id().getText();
		String type = ctx.param_decl().var_type().getText();
		Variable declaration = new Variable(name, type);
		if(duplicateDeclaration(declaration))
		{
			System.exit(0);
		}
		
		//Continue extractering further parameters
		LittleParser.Param_decl_tailContext paramTail = ctx.param_decl_tail();
		while(paramTail.getText() != "")
		{
			name = paramTail.param_decl().id().getText();
			type = paramTail.param_decl().var_type().getText();
			declaration = new Variable(name, type);
			if(duplicateDeclaration(declaration))
			{
				System.exit(0);
			}
			paramTail = paramTail.param_decl_tail();
		}
	}
	
	public boolean duplicateDeclaration(Variable declaration)
	{
		if(scopeStack.peek().contains(declaration))
		{
			System.out.println("DECLARATION ERROR "+declaration.getName());
			return true;
		}else
		{
			scopeStack.peek().add(declaration);
			return false;
		}
	}
	
	public void PrettyPrint()

	{
		//print all symbol tables in the order they were created
		for(int i = 0; i < symbolTable.size(); i++)
		{
			ArrayList<Variable> currentTable = symbolTable.get(i);
			String tableName = currentTable.get(0).getName();
			System.out.println("Symbol table "+tableName);
			
			for(int y = 1; y < currentTable.size(); y++)
			{
				String name = currentTable.get(y).getName();
				String type = currentTable.get(y).getType();
				String value = currentTable.get(y).getValue();
				if(value == null)
				{
					System.out.println("name "+name+" type "+type);
				}else
				{
					System.out.println("name "+name+" type "+type+" value "+value);
				}
			}
			if(i != symbolTable.size() - 1)
			{
				System.out.println();
			}
		}
	}
	
	
}

class Variable
{
	// Variable Declaration attributes
	private String name;
	private String type;
	private String value;
	
	// String variable constructor
	public Variable(String name, String type, String value)
	{
		this.name = name;
		this.type = type;
		this.value = value;
	}
	
	// Float/Int variable constructor
	public Variable(String name, String type)
	{
		this.name = name;
		this.type = type;
		this.value = null;
	}
	
	// Table variable constructor
	public Variable(String name)
	{
		this.name = name;
		this.type = null;
		this.type = null;
	}
	
	// getter methods
	public String getName()
	{
		return this.name;
	}
	
	public String getType()
	{
		return this.type;
	}
	
	public String getValue()
	{
		return this.value;
	}
	
	// Equals method
	
	@Override public boolean equals(Object obj)
	{
		Variable test = (Variable)obj;
		return this.name.equals(test.getName());
	}
	
}