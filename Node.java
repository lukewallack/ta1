// This class, and its subclasses,
// collectively model parse-tree nodes.
// Each kind of node can be eval()-uated,
// and/or code()-generated.

public abstract class Node {

	protected int pos=0;

	// Evaluate an input from the current program's environment.
	// Returns the resulting value in valid implementations, but 
	// here we just throw an exception since we cannot evaluate 
	// a default Node object. Each Node object describes an element
	// in our grammar, and together they define our entire grammar.
	// Arguments:
	//     env = the current program environment
	// Members:
	//     pos = the position in the program's input
	// Returns:
	//     Value of the node (in implemented classes)
	// Throws:
	//     EvalException when called, cannot eval a Node object.
	public double eval(Environment env) throws EvalException {
		throw new EvalException(pos,"cannot eval() node!");
	}

	// Return a C code generation for the current node.
	public String code() { return ""; }

}
