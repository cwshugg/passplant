package passgen;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 * A class that serves as the UI for the user. Takes
 * in user input and passes it along to other classes
 * to get passwords generated.
 * 
 * @author Connor Shugg
 * @version 2019-2-15
 */
public class Prompter
{	
	// NOTE TO SELF:
	// Ask the user questions such as:
	//  -Is there a certain range you want?
	//	  -If so, what is it? (upper/lower bounds)
	//	-Do you want numbers in the password?
	//	-Do you want symbols in the password?
	//	-Do you want underscores in the password?
	//  -How many passwords do you want?
	
	// Command-line argument list:
	// "quick <X>"		Tells the program to skip everything and
	//					just generate X number of passwords.
	
	/**
	 * Main method - takes in command-line arguments to help
	 * the user specify what they want to do
	 * @param args - the String array of command-line arguments
	 */
	public static void main(String[] args)
	{
		System.out.println("------------------------------------------------------------");
		System.out.println("---------------[Snowflake Password Generator]---------------");
		System.out.println("------------------------------------------------------------\n");
		
		// create the password generator
		Generator pgen = new Generator();
		
		// ARGUMENTS: look for "quick"
		int quickIndex = findArgument(args, "quick");
		if (quickIndex >= 0)
		{
			// try to read the number of passwords to generate as the next argument
			try
			{
				int numPasswords = Integer.parseInt(args[quickIndex + 1]);				
				System.out.println("Quick-generation: generating " + numPasswords + " passwords...");
				
				// create the passwords and print them out
				printPasswords(pgen.makePasswords(numPasswords));
			}
			catch (Exception e)
			{
				System.out.println("Quick-generation switch failed: Must be in the format: \"quick X\"");
			}
		}
	}
	
	
	/**
	 * Helper function that takes in the command-line arguments, and the
	 * name of the argument to search for. If it's found, the index in args[]
	 * is returned
	 * @param args - the command-line argument array
	 * @param findMe - the argument to find in the array
	 * @return the index findMe was found at, or -1 if not found
	 */
	private static int findArgument(String[] args, String findMe)
	{
		int index = -1;
		
		// iterate through the array and search for the argument
		for (int i = 0; i < args.length; i++)
		{
			if (args[i].contains(findMe))
			{ index = i; }
		}
		
		return index;
	}

	
	// ------------------- Printing Methods ------------------- //
	/**
	 * Helper function that prints a string, either to the command-line
	 * output, or a specified file. (If the given file is null, the function
	 * will print to the standard output instead.) The string will be appended
	 * to the given file
	 * @param printMe - the string to print
	 * @param filePath - the path to the file to append to
	 */
	private static void printString(String printMe, String filePath)
	{
		if (filePath == null)
		{
			// if the file path is null, just print
			// the string to command-line output
			System.out.print(printMe);
		}
		else
		{
			// if the file path isn't null, try opening up and appending
			// the string to it. If this fails, an IOException will
			// be caught and reported.
			try
			{
				BufferedWriter file = new BufferedWriter(new FileWriter(filePath, true));
				file.write(printMe);
				file.close();
			}
			catch (IOException e)
			{
				System.out.println("! Exception occurred while writing to file.");
			}
		}
	}
	
	/**
	 * Helper method that prints out a list of given passwords
	 * to the command-line (or maybe a file)
	 * @param passwords - the array of passwords to print
	 */
	private static void printPasswords(String[] passwords)
	{
		//printString("----------[Passwords]----------\n", null);
		
		// print out each password on its own line
		for (int i = 0; i < passwords.length; i++)
		{
			printString("\n" + passwords[i], null);
		}
		
		//printString("\n\n-------------------------------\n", null);
	}
	
}
