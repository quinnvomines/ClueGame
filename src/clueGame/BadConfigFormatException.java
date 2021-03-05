package clueGame;

import java.io.FileNotFoundException;
import java.io.PrintWriter;

//exception class
public class BadConfigFormatException extends Exception{

	public BadConfigFormatException() {
		super("BadConfigFormatException");
	}

	public BadConfigFormatException(String message) {
		super(message);

		//Print to Exception to logfile.txt
		PrintWriter out;
		try {
			out = new PrintWriter("logfile.txt");
			out.println(message);
			out.close();
		} catch (FileNotFoundException e) {
			e.getMessage(); //Print FileNotFoundException
		}

	}

	public String toString() {
		return "BadConfigFormatException, check format of input files";
	}

}
