public class NodeFactMinus extends NodeFact {

	private NodeFact fact; // operand which we perform unary minus on

	public NodeFactMinus(NodeFact fact) {
		this.fact=fact;
	}

	// This method evaluates the factor, returning itself negated.
	// Arguments:
	//     env = the current program environment
	// Members:
	//     fact = the factor/operand which we negate
	// Returns:
	//     fact negated
	public double eval(Environment env) throws EvalException {
		return -fact.eval(env);
	}

	public String code() { return "(-"+fact.code()+")"; }

}
