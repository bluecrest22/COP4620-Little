// import ANTLR's runtime libraries
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.misc.ParseCancellationException;
import org.antlr.v4.runtime.tree.ErrorNode;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeListener;
import org.antlr.v4.runtime.tree.ParseTreeProperty;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.antlr.v4.runtime.tree.TerminalNode;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class Driver
{
	public static void main(String[] args) throws Exception 
	{
		// Establish character stream from System.in to Lexer
		CharStream input = CharStreams.fromStream(System.in);
		LittleLexer lexer = new LittleLexer(input);
		
		// Capture tokens from lexer
		CommonTokenStream tokens = new CommonTokenStream(lexer);
		
		// Creates a parser that feeds off the token buffer
		LittleParser parser = new LittleParser(tokens);
		ParseTree tree = parser.program();
		
		// Create a generic parse tree walker that can trigger callbacks
		ParseTreeWalker walker = new ParseTreeWalker();
		SimpleTableBuilder symbolObject = new SimpleTableBuilder();
		ArrayList<ArrayList<Variable>> symbolTable = symbolObject.getSymbolTable();
		SimpleAbstractSyntaxTree syntaxTreeObject = new SimpleAbstractSyntaxTree(symbolTable);
		
		// Walk the tree created during the parse, trigger callbacks
		walker.walk(symbolObject, tree);
		walker.walk(syntaxTreeObject, tree);
		
		// Generate Three Adress Code
		ThreeAddressCodeGenerator CodeObject = new ThreeAddressCodeGenerator(syntaxTreeObject.getRoot());
		List<CodeObject> codeList = CodeObject.GenerateThreeAdressCodeList();
		
		// Generate Tiny Code
		TinyCodeGenerator tinyObject = new TinyCodeGenerator(symbolTable, codeList);
		
		// Populate ArrayList with instructions
		ArrayList<String> instrucList = new ArrayList<String>();
		CodeObject.DislayCodeObject(instrucList);
		tinyObject.GenerateTinyCode(instrucList);
		
		// Creates a file for output
	    try 
	    {
	        File myObj = new File("filename.txt");
	        if (myObj.createNewFile()) 
	        {
	          System.out.println("File created: " + myObj.getName());
	        } else 
	        {
	          System.out.println("File already exists.");
	        }
	    } catch (IOException e) 
	    {
	        System.out.println("An error occurred.");
	        e.printStackTrace();
	    }
	    
	    try 
	    {
	        FileWriter myWriter = new FileWriter("filename.txt");
	        for(String line : instrucList)
	        {
	        	myWriter.write(line+"\n");
	        }
	        myWriter.close();
	    } catch (IOException e) 
	    {
	        System.out.println("An error occurred.");
	        e.printStackTrace();
	    }
	}
}
