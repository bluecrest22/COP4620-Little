// import ANTLR's runtime libraries
import org.antlr.v4.runtime.*;
import java.io.PrintWriter;

public class Driver
{

	public static void main(String[] args) throws Exception 
	{
		// Establish character stream from System.in to Lexer
		CharStream input = CharStreams.fromStream(System.in);
		LittleLexer lexer = new LittleLexer(input);
		
		// Capture tokens from lexer
		CommonTokenStream tokens = new CommonTokenStream(lexer);
		tokens.fill();
		
		// Output To StdOut
		PrintWriter printWriter = new PrintWriter(System.out);
		
		
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
	}
	

}


