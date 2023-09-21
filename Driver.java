// import ANTLR's runtime libraries
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.misc.ParseCancellationException;

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
		
		// Prevents extraneous output to console
		parser.removeErrorListeners();
		
		// Terminates parsing once parser finds syntax error
		parser.setErrorHandler(new BailErrorStrategy());
		
		// Catches Syntax Errors
		try
		{
			parser.program();
		}
		catch(ParseCancellationException e)
		{
			System.out.println("Not accepted");
			System.exit(0);	
		}
		
		System.out.println("Accepted");
	}
}


