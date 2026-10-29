import java.io.*;

public class Code {

	private final String[] prologue={
		"#include <stdio.h>",
		"int main() {",
	};

	private final String[] epilogue={
		"return 0;",
		"}",
	};

	// Constructor: convert provided code in an environment `env`
	// into C code based on the provided environment and its variables.
	public Code(String code, Environment env) {
		String fn=System.getenv("Code");
		if (fn==null)
			return;
		try {
			BufferedWriter f=new BufferedWriter(new FileWriter(fn+".c"));
			for (String s: prologue)
				f.write(s+"\n");
			f.write(env.toC());
			f.write(code);
			for (String s: epilogue)
				f.write(s+"\n");
			f.close();
		} catch (Exception e) {
			System.err.println(e);
		}
	}

}
