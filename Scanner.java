// This class is a scanner for the program
// and programming language being interpreted.

import java.util.*;

public class Scanner {

	private String program;		// source program being interpreted
	private int pos;			// index of next char in program
	private Token token;		// last/current scanned token

	// sets of various characters and lexemes
	// initialized in constructor via corresponding init methods
	private Set<String> whitespace=new HashSet<String>();
	private Set<String> digits=new HashSet<String>();
	private Set<String> letters=new HashSet<String>();
	private Set<String> legits=new HashSet<String>();
	private Set<String> keywords=new HashSet<String>();
	private Set<String> operators=new HashSet<String>();
	private Set<String> comments = new HashSet<String>(); // Probably unnecessary but maybe useful later?

	// initializers for previous sets

	// Fills a set with characters from the provided low end `lo`
	// up to the high end `hi`. For example, filling with 'a' to 'z' 
	// would fill the set `s` with characters a, b, ..., z.
	// Arguments:
	//     s = set to fill
	//     lo = low end char starting point
	//     hi = high end char end point
	private void fill(Set<String> s, char lo, char hi) {
		for (char c=lo; c<=hi; c++)
			s.add(c+"");
	}

	// Initializes the set of whitespace characters
	// space, newline, and tab.
	private void initWhitespace(Set<String> s) {
		s.add(" ");
		s.add("\n");
		s.add("\t");
	}

	// Initializes the set of digits 0-9.
	private void initDigits(Set<String> s) {
		fill(s,'0','9');
	}

	// Initializes the set of letters a-z/A-Z.
	private void initLetters(Set<String> s) {
		fill(s,'A','Z');
		fill(s,'a','z');
	}

	// Initializes the set of characters allowed (letters and digits)
	private void initLegits(Set<String> s) {
		s.addAll(letters);
		s.addAll(digits);
	}

	// Initializes set of operator lexemes
	// = + - * / ( ) ;
	private void initOperators(Set<String> s) {
		s.add("=");
		s.add("+");
		s.add("-");
		s.add("*");
		s.add("/");
		s.add("(");
		s.add(")");
		s.add(";");
	}

	// Initializes set of keywords.
	private void initKeywords(Set<String> s) {
	}

	// Initializes set of comment characters. Currently, 
	// comments simply begin with a `#` and go until the 
	// end of the line. Later, this may change, so I thought 
	// it'd be useful to create a set for this.
	private void initComments(Set<String> s) {
		s.add("#");
	}

	// constructor:
	//     - squirrel-away source program
	//     - initialize sets
	// Arguments:
	//     program = the source program to scan
	public Scanner(String program) {
		this.program=program;
		pos=0;
		token=null;
		initWhitespace(whitespace);
		initDigits(digits);
		initLetters(letters);
		initLegits(legits);
		initKeywords(keywords);
		initOperators(operators);
		initComments(comments);
	}

	// handy string-processing methods

	// Returns true when the input stream has been fully scanned.
	// Members:
	//     pos = index of current input character
	//     program = the scanner's input
	public boolean done() {
		return pos>=program.length();
	}

	// This method advances the scanner past a set of s
	// characters. Stops advancing after the first character 
	// not in set s.
	// Arguments:
	//     s = the set of characters to skip
	// Members:
	//     program = the scanner's input
	//     pos = index of current input character
	private void many(Set<String> s) {
		while (!done()&&s.contains(program.charAt(pos)+""))
			pos++;
	}

	// This method advances the scanner,
	// until the current input character
	// is just after a sequence of one or more
	// of a particular character.
	// Arguments:
	//     c = the character to search for
	// Members:
	//     program = the scanner's input
	//     pos = index of current input character
	private void past(char c) {
		while (!done()&&c!=program.charAt(pos))
			pos++;
		if (!done()&&c==program.charAt(pos))
			pos++;
	}

	// scan various kinds of lexeme

	// Scans a number lexeme based on the set of digits.
	// Supports both integers and doubles.
	// Members:
	//     pos = advanced past the digits
	//     token = new Token "num" with lexeme being the scanned digits.
	private void nextNumber() {
		int old=pos;
		many(digits);
		if (!done() && program.charAt(pos) == '.') { // Scan double if '.' present
			pos++;			// Advance past '.'
			many(digits);	// Scan further digits
		}
		token=new Token("num",program.substring(old,pos));
	}

	// Scans a keyword/identifier starting with a letter 
	// followed by any letters/digits. If the lexeme is a valid 
	// keyword, then the token is the keyword, otherwise it is set to
	// "id".
	// Members:
	//     pos = advanced past the letters/digits
	//     token = new Token keyword or "id"
	private void nextKwId() {
		int old=pos;
		many(letters);
		many(legits);
		String lexeme=program.substring(old,pos);
		token=new Token((keywords.contains(lexeme) ? lexeme : "id"),lexeme);
	}

	// Scans an operator based on the set of valid operators.
	// Starts by trying to scan two characters, falling back to 
	// one character if none exists. The token is the lexeme itself.
	// Members:
	//     pos = advanced past scanned operator (either 1 or 2 characters)
	//     token = new Token determined by lexeme
	private void nextOp() {
		int old=pos;
		pos=old+2;
		if (!done()) {
			String lexeme=program.substring(old,pos);
			if (operators.contains(lexeme)) {
				token=new Token(lexeme); // two-char operator
				return;
			}
		}
		pos=old+1;
		String lexeme=program.substring(old,pos);
		token=new Token(lexeme); // one-char operator
	}

	// This method determines the kind of the next token (e.g., "id"),
	// and calls a method to scan that token's lexeme (e.g., "foo").
	// Comments and whitespace are discarded before the tokens are scanned.
	public boolean next() {
		many(whitespace);
		// Discard comments:
		while (!done() && comments.contains(program.charAt(pos)+"")) { // If next char is a comment
			past('\n'); 											// Advance past it until end of line
			many(whitespace); 										// Advance past any remaining whitespace
		}
		if (done()) {
			token=new Token("EOF");
			return false;
		}
		String c=program.charAt(pos)+"";
		if (digits.contains(c))
			nextNumber();
		else if (letters.contains(c))
			nextKwId();
		else if (operators.contains(c))
			nextOp();
		else {
			System.err.println("illegal character at position "+pos);
			pos++;
			return next();
		}
		return true;
	}

	// This method scans the next lexeme,
	// if the current token is the expected token.
	public void match(Token t) throws SyntaxException {
		if (!t.equals(curr()))
			throw new SyntaxException(pos,t,curr());
		next();
	}

	// Returns the most recently scanned Token.
	// Throws:
	//     SyntaxException when token is null (no scanned tokens)
	public Token curr() throws SyntaxException {
		if (token==null)
			throw new SyntaxException(pos,new Token("ANY"),new Token("EMPTY"));
		return token;
	}

	// Getter that returns pos, the index of the next 
	// input character.
	public int pos() {
		return pos;
	}

	// for unit testing
	public static void main(String[] args) {
		try {
			Scanner scanner=new Scanner(args[0]);
			while (scanner.next())
				System.out.println(scanner.curr());
		} catch (SyntaxException e) {
			System.err.println(e);
		}
	}

}
