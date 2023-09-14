/**
 * COP4620 
 * Little Grammar
 * Step 1
 */
grammar Little;

r      : OPERATOR
	   | KEYWORD
	   | IDENTIFIER
	   | INTLITERAL
	   | FLOATLITERAL
	   | STRINGLITERAL
	   ;

	   
//Operators := + - * / = != < > ( ) ; , <= >=
OPERATOR   : ':='
		   | '+'
		   | '-'
		   | '*'
		   | '/'
		   | '='
		   | '!='
		   | '<'
	       | '>'
	       | '('
		   | ')'
	       | ';'
	       | ','
		   | '<='
		   | '>='
	  	   ;
	  	   
//Keywords PROGRAM,BEGIN,END,FUNCTION,READ,WRITE, IF,ELSE,ENDIF,WHILE,ENDWHILE,CONTINUE,BREAK, 
// RETURN,INT,VOID,STRING,FLOAT*/
KEYWORD	 : 'PROGRAM' 
	 	 | 'BEGIN' 
      	 | 'END' 
 		 | 'FUNCTION'
     	 | 'READ'
    	 | 'WRITE'
     	 | 'IF' 
	 	 | 'ELSE' 
         | 'ENDIF'
	 	 | 'WHILE'
 		 | 'ENDWHILE'
 		 | 'CONTINUE'
    	 | 'BREAK'
   		 | 'RETURN'
		 | 'INT'
     	 | 'VOID'
   		 | 'STRING'
    	 | 'FLOAT'
    	 ;
    	 
//an IDENTIFIER token will begin with a letter, and be followed by any
//number of letters and numbers.
//IDENTIFIERS are case sensitive*/
IDENTIFIER  : ('a'..'z'|'A'..'Z')('a'..'z'|'A'..'Z'|'0'..'9')* ;

// INTLITERAL: integer number ex) 0, 123, 678
INTLITERAL  : ('0'..'9')+ ;

// fLOATLITERAL: floating point number available in two different format yyyy.xxxxxx or .xxxxxxxex) 3.141592 , .1414 , .0001 , 456.98
FLOATLITERAL : ('0'..'9')+ '.' ('0'..'9')*
		|		 '.' ('0'..'9')+
		;
		
// STRINGLITERAL: any sequence of characters except '"'between '"' and '"'ex) "Hello world!" , "***********" , "this is a string"
STRINGLITERAL  : '"' (ESC | . )*? '"' ;
fragment ESC : '\\' [btnr"\\] ; // \b, \t, \n etc...

//COMMENT: 
//Starts with "--" and lasts till the end of line
//ex) -- this is a comment
//ex) -- any thing after the "--" is ignored
LINE_COMMENT : '--' .*? '\r'? '\n' -> skip ;

// Match whitespace and throw it out
WS : [ \t\r\n]+ -> skip ; // skip spaces, tabs, newlines

