// import ANTLR's runtime libraries
import org.antlr.v4.runtime.*;
import java.io.FileWriter;
//import java.io.IOException;
import java.io.PrintWriter;

public class Driver
{

	public static void main(String[] args) throws Exception 
	{
		// Input File
		// Inputs from ./Micro.sh are System.in not file names
		//String inputFile = null;
		//if ( args.length>0 ) inputFile = args[0];

		// Establish character stream from file to Lexer
		CharStream input = CharStreams.fromStream(System.in);
		LittleLexer lexer = new LittleLexer(input);
		
		// Capture tokens from lexer
		CommonTokenStream tokens = new CommonTokenStream(lexer);
		tokens.fill();
		
		// Output File
		String outputFile = "output.txt";//linputFile.replace("micro", "out");
		FileWriter outputWriter = new FileWriter(outputFile);
		PrintWriter printWriter = new PrintWriter(outputWriter);
		
		
		Vocabulary lexerVocab = lexer.getVocabulary();
		int tokenListSize = tokens.getTokens().size();
		int tokenNumber = 0;
		for (Token var : tokens.getTokens())
		{
			tokenNumber++;
			if(tokenNumber == tokenListSize) {break;}
			printWriter.printf("Token Type: %s\n", lexerVocab.getSymbolicName(var.getType()));
			printWriter.printf("Value: %s\n", var.getText());
		}
		
		// Closing writing resources
		printWriter.close();
		outputWriter.close();
	}
	

}


