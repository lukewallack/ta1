// This class provides a stubbed-out environment.
// You are expected to implement the methods.
// Accessing an undefined variable should throw an exception.

// Hint!
// Use the Java API to implement your Environment.
// Browse:
//   https://docs.oracle.com/javase/tutorial/tutorialLearningPaths.html
// Read about Collections.
// Focus on the Map interface and HashMap implementation.
// Also:
//   https://www.tutorialspoint.com/java/java_map_interface.htm
//   http://www.javatpoint.com/java-map
// and elsewhere.
import java.util.*;

public class Environment {

	private Map<String, Double> map = new HashMap<String, Double>(); // map variables in our env to double values

	// Inserts a key value pair into the map hashmap, with the variable 
	// name as the key and double `val` as the value.
	// Arguments:
	//     var = variable name
	//     val = double value to be stored, associated with var
	// Members:
	//     map = HashMap containing the environment's variables
	// Returns:
	//     the value stored
	public double put(String var, double val) {
		map.put(var, val);
		return val;
	}

	// Returns a variable's value, throwing an exception if 
	// no such variable exists.
	// Arguments:
	//     pos = current position of program
	//     var = variable name, key in hashmap
	// Members:
	//     map = HashMap containing the environment's variables
	// Returns:
	//     double value associated with key `var`.
	// Throws:
	//     EvalException when key `var` is not in map.
	public double get(int pos, String var) throws EvalException {
		if (!map.containsKey(var))
			throw new EvalException(pos, var);
		return map.get(var);
	}

	// This method generates a C program declaring the variables 
	// in the environment, all of which are doubles.
	// Members:
	//     map = HashMap containing the environment's variables
	// Returns:
	//     A C source containing each variable in our environment.
	public String toC() {
		String s = "";
		String sep = " ";
		for (String v : map.keySet()) {
			s += sep + v;
			sep = ",";
		}
		return s == "" ? "" : "double" + s + ";\nx=0;x=x;\n";
	}

}
