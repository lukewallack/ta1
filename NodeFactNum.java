public class NodeFactNum extends NodeFact {

	private String num;

	public NodeFactNum(String num) {
		this.num=num;
	}

	public double eval(Environment env) throws EvalException {
		return Double.parseDouble(num);
	}

	// Returns the C formatted code version of our Node.
	// In this case, we convert number factors to doubles 
	// to address a bug where dividing always results in integer 
	// division in the C code.
	public String code() { return Double.toString(Double.parseDouble(num)); }

}
