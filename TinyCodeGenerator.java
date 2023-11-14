import java.util.ArrayList;
import java.util.List;

public class TinyCodeGenerator 
{
	List<CodeObject> CodeObjectList;
	ArrayList<ArrayList<Variable>> symbolTable;
	int registerNumber = 0;
	
	//Constructor
	public TinyCodeGenerator(ArrayList<ArrayList<Variable>> symbolTable, List<CodeObject> CodeObjectList)
	{
		this.symbolTable = symbolTable;
		this.CodeObjectList = CodeObjectList;
	}
	
	public void GenerateTinyCode(ArrayList<String> instrucList)
	{
		instrucList.add(";tiny code");
		for(int i = 1; i < symbolTable.get(0).size(); i++)
		{
			Variable var = symbolTable.get(0).get(i);
			if(var.getType().equals("STRING"))
			{
				instrucList.add("str " +var.getName()+" "+var.getValue());
			}else
			{
				instrucList.add("var "+var.getName());
			}
		}
		
		for(CodeObject code : CodeObjectList)
		{
			GenerateTinyInstructions(code, instrucList);
		}
		
		instrucList.add("sys halt");
	}
	
	public void GenerateTinyInstructions(CodeObject code, ArrayList<String> instrucList)
	{
		CodeObject.opcode type = code.getOpcode();
		String arg1 = code.getArg1();
		String arg2 = code.getArg2();
		String result = code.getResult();
		
		if(result != null && result.contains("$T"))
		{
			result = result.replace("$T", "");
			result = "r" + String.valueOf(Integer.parseInt(result) - 1);
			
		}if(arg1 != null && arg1.contains("$T"))
		{
			arg1 = arg1.replace("$T", "");
			arg1 = "r" + String.valueOf(Integer.parseInt(arg1) - 1);
		}if(arg2 != null && arg2.contains("$T"))
		{
			arg2 = arg2.replace("$T", "");
			arg2 = "r" + String.valueOf(Integer.parseInt(arg2) - 1);
		}
		
		switch(type)
		{
			case ADDI:
				instrucList.add("move "+arg1+" "+result);
				instrucList.add("addi "+arg2+" "+result);
				break;
			case SUBI:
				instrucList.add("move "+arg1+" "+result);
				instrucList.add("subi "+arg2+" "+result);
				break;
			case MULTI:
				instrucList.add("move "+arg1+" "+result);
				instrucList.add("muli "+arg2+" "+result);
				break;
			case DIVI:
				instrucList.add("move "+arg1+" "+result);
				instrucList.add("divi "+arg2+" "+result);
				break;
			case ADDF:
				instrucList.add("move "+arg1+" "+result);
				instrucList.add("addr "+arg2+" "+result);
				break;
			case SUBF:
				instrucList.add("move "+arg1+" "+result);
				instrucList.add("subr "+arg2+" "+result);
				break;
			case MULTF:
				instrucList.add("move "+arg1+" "+result);
				instrucList.add("mulr "+arg2+" "+result);
				break;
			case DIVF:
				instrucList.add("move "+arg1+" "+result);
				instrucList.add("divr "+arg2+" "+result);
				break;
			case STOREI:
			case STOREF:
				instrucList.add("move "+arg1+" "+result);
				break;
			case READI:
				instrucList.add("sys readi "+result);
				break;
			case READF:
				instrucList.add("sys rear "+result);
				break;
			case WRITEI:
				instrucList.add("sys writei "+arg1);
				break;
			case WRITEF:
				instrucList.add("sys writer "+arg1);
				break;
			case WRITES:
				instrucList.add("sys writes "+arg1);
				break;
		}
	}
	
}
