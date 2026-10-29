// This class models a token, which has two parts:
// 1) the token itself (e.g., "id" or "+")
// 2) the token's lexeme (e.g., "foo")

public class Token {

	private String token;	// Token type
	private String lexeme;	// Token lexeme

	// Constructor: create a new token given a type and lexeme.
	public Token(String token, String lexeme) {
		this.token=token;
		this.lexeme=lexeme;
	}

	// Constructor 2: create a new token given only a token type.
	public Token(String token) {
		this(token,token);
	}

	// Returns the token's type.
	public String tok() { return token; }

	// Returns the token's lexeme.
	public String lex() { return lexeme; }

	// Returns true if the two tokens share a token type.
	public boolean equals(Token t) {
		return token.equals(t.token);
	}

	// <token, lexeme>
	public String toString() {
		return "<"+tok()+","+lex()+">";
	}

}
