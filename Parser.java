// This class is a recursive-descent parser,
// modeled after the programming language's grammar.
// It constructs and has-a Scanner for the program
// being parsed.

public class Parser {

	private Scanner scanner;

	// This method checks that the current token matches a specified
	// token `s`. If it does, advances the scanner to the next token.
	// Arguments:
	//     s = the token type expected ('id', '+', etc)
	// Members:
	//     scanner = scanner used to parse the program
	// Throws:
	//     SyntaxException when token does not match `s`.
	private void match(String s) throws SyntaxException {
		scanner.match(new Token(s));
	}
	
	// This method parses multiplier operators (* /) and 
	// returns a new Node representing the operator that 
	// was parsed when successful. Otherwise, returns null.
	// Members:
	//     scanner = scanner used to parse the program
	// Returns: 
	//     the most recently scanned token
	// Throws:
	//     SyntaxException when no token has been scanned yet
	private Token curr() throws SyntaxException {
		return scanner.curr();
	}

	// Returns the scanner's current position within the program.
	// Members:
	//     scanner = scanner used to parse the program
	// Returns: 
	//     the position of the scanner in the program
	private int pos() {
		return scanner.pos();
	}

	// This method parses multiplier operators (* /) and 
	// returns a new Node representing the operator that 
	// was parsed when successful. Otherwise, returns null.
	// Members:
	//     scanner = advanced past parsed operator
	// Returns: 
	//     NodeMulop representing the successfully parsed 
	//     operator, otherwise null.
	// Throws:
	//     SyntaxException when no token has been scanned (see curr)
	private NodeMulop parseMulop() throws SyntaxException {
		if (curr().equals(new Token("*"))) {
			match("*");
			return new NodeMulop(pos(), "*");
		}
		if (curr().equals(new Token("/"))) {
			match("/");
			return new NodeMulop(pos(), "/");
		}
		return null;
	}

	// This method parses addition operators (+ -) and 
	// returns a new Node representing the operator that 
	// was parsed when successful. Otherwise, returns null.
	// Members:
	//     scanner = advanced past parsed operator
	// Returns: 
	//     NodeAddop representing the successfully parsed 
	//     operator, otherwise null.
	// Throws:
	//     SyntaxException when no token has been scanned (see curr)
	private NodeAddop parseAddop() throws SyntaxException {
		if (curr().equals(new Token("+"))) {
			match("+");
			return new NodeAddop(pos(), "+");
		}
		if (curr().equals(new Token("-"))) {
			match("-");
			return new NodeAddop(pos(), "-");
		}
		return null;
	}

	// This method parses a factor using our grammar's rules.
	// A factor is defined by the grammar: 
	// fact  : id 
	// 		 | num
	// 		 | '(' expr ')'
	// 		 | '-' fact
	// Members:
	//     scanner = advanced past parsed factor
	// Returns: 
	//     Corresponding NodeFact type representing factor:
	//	   NodeFactMinus = '-' fact
	//     NodeFactExpr  = '(' expr ')'
	//     NodeFactId	 = id
	//	   NodeFactNum   = num
	// Throws:
	//     SyntaxException when the current token does not match
	//     a factor type.
	private NodeFact parseFact() throws SyntaxException {
		if (curr().equals(new Token("-"))) { // Parse unary minus before anything else
			match("-");
			NodeFact fact = parseFact();
			return new NodeFactMinus(fact);
		}
		if (curr().equals(new Token("("))) {
			match("(");
			NodeExpr expr = parseExpr();
			match(")");
			return new NodeFactExpr(expr);
		}
		if (curr().equals(new Token("id"))) {
			Token id = curr();
			match("id");
			return new NodeFactId(pos(), id.lex());
		}
		Token num = curr();
		match("num");
		return new NodeFactNum(num.lex());
	}

	// This method parses a term determined by our grammar:
	// term  : fact mulop term
	// 		 | fact
	// Members:
	//     scanner = advanced past parsed term
	// Returns: 
	//     NodeTerm representing the parsed term
	// Throws:
	//     SyntaxException if no matching factor
	private NodeTerm parseTerm() throws SyntaxException {
		NodeFact fact = parseFact();
		NodeMulop mulop = parseMulop();
		if (mulop == null)
			return new NodeTerm(fact, null, null);
		NodeTerm term = parseTerm();
		term.append(new NodeTerm(fact, mulop, null));
		return term;
	}

	// This method parses an expression determined by our grammar:
	// expr  : term addop expr
	// 		 | term
	// Members:
	//     scanner = advanced past parsed expression
	// Returns: 
	//     NodeExpr representing the parsed expression
	// Throws:
	//     SyntaxException if no matching expression
	private NodeExpr parseExpr() throws SyntaxException {
		NodeTerm term = parseTerm();
		NodeAddop addop = parseAddop();
		if (addop == null)
			return new NodeExpr(term, null, null);
		NodeExpr expr = parseExpr();
		expr.append(new NodeExpr(term, addop, null));
		return expr;
	}

	// This method parses an assignmnet determined by our grammar:
	// assn  : id '=' expr
	// Members:
	//     scanner = advanced past parsed assignment
	// Returns: 
	//     NodeAssn representing the variable and expression
	// Throws:
	//     SyntaxException if no matching expression
	private NodeAssn parseAssn() throws SyntaxException {
		Token id = curr();
		match("id");
		match("=");
		NodeExpr expr = parseExpr();
		NodeAssn assn = new NodeAssn(id.lex(), expr);
		return assn;
	}

	// This method parses a statement determined by our grammar:
	// stmt  : assn ';'
	// Members:
	//     scanner = advanced past parsed statement
	// Returns: 
	//     NodeStmt representing the parsed statement
	// Throws:
	//     SyntaxException if no matching expression
	private NodeStmt parseStmt() throws SyntaxException {
		NodeAssn assn = parseAssn();
		match(";");
		NodeStmt stmt = new NodeStmt(assn);
		return stmt;
	}

	// This method parses a whole program. It starts by creating a
	// scanner for the current program, parsing a statement,
	// and continuing until it finds 'EOF' (program end). Parses 
	// according to our whole grammar.
	// Members:
	//     scanner = new scanner for the specified source program
	// Returns: 
	//     NodeStmt a parsed statement
	// Throws:
	//     SyntaxException if program does not match the grammar
	public Node parse(String program) throws SyntaxException {
		scanner = new Scanner(program);
		scanner.next();
		NodeStmt stmt = parseStmt();
		match("EOF");
		return stmt;
	}

}
