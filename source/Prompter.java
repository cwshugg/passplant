package passgen;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

/**
 * A class that serves as the UI for the user. Takes
 * in user input and passes it along to other classes
 * to get passwords generated.
 * 
 * @author Connor Shugg
 * @version 2019-2-18
 */
public class Prompter
{	
	private static Scanner scanner;
	
	// NOTE TO SELF:
	// Ask the user questions such as:
	//  -Is there a certain range you want?
	//	  -If so, what is it? (upper/lower bounds)
	//	-Do you want numbers in the password?
	//	-Do you want symbols in the password?
	//	-Do you want underscores in the password?
	//  -How many passwords do you want?
	
	// Command-line argument list:
	// "quick <X>"				Tells the program to skip everything and
	//							just generate X number of passwords.
	// "numbers <on/off>"		Turns numbers on/off in the password generator (off by default)
	// "symbols <on/off>"		Turns symbols on/off in the password generator (off by default)
	// "underscores <on/off>"	Turns underscores on/off in the password generator (off by default)
	
	/**
	 * Enum used by print methods to determine what
	 * prefix to give a message being printed to the
	 * console.
	 * 
	 * @author Connor Shugg
	 * @version 2019-2-18
	 */
	private enum MessageType
	{ PLAIN, STANDARD, ERROR, HEADER, DIALOGUE }
	
	/**
	 * Main method - takes in command-line arguments to help
	 * the user specify what they want to do
	 * @param args - the String array of command-line arguments
	 */
	public static void main(String[] args)
	{
		printConsoleLine(MessageType.PLAIN, "");
		printConsoleLine(MessageType.HEADER, "---------------------------------------");
		printConsoleLine(MessageType.HEADER, "Welcome to Snowflake Password Generator");
		printConsoleLine(MessageType.HEADER, "---------------------------------------");
		
		// if any of the code here fails, alert the user
		try
		{
			// create the password generator
			Generator pgen = new Generator();
			
			// apply user preferences, based on the arguments
			pgen = applyUserPreferences(args, pgen);
			
			// check for the "quick" argument. If so, quickly
			// generate some passwords in this method:
			boolean generated = checkQuickGeneration(args, pgen);
			
			// if passwords weren't generated, move onto the
			// "main" program
			if (!generated)
			{
				mainThread(pgen);
			}
		
		}
		catch (Exception e)
		{
			printErrorMessage();
		}
	}
	
	
	// -------------------- Main Processes ------------------- //
	/**
	 * The "main thread" of the program. Follows a set of user prompts
	 * to get passwords generated.
	 * @param gen - the generator to use for password creation
	 */
	private static void mainThread(Generator gen)
	{
		// open the scanner
		scanner = new Scanner(System.in);
		//--------------------------------
		
		// retrieve the UserData
		UserData ud = gen.getUserData();
		
		// if the user's name is null, have the user set it up
		if (ud.getUserName() == null)
		{
			ud.setUserName(promptUserName());
			ud.saveUserData();
		}
		else
		{
			// otherwise, get the snowman to say hello!
			printSnowman();
			printConsoleLine(MessageType.DIALOGUE, "Hello, " + ud.getUserName() + ". It's me, Dan the Snowman.");
			printConsoleLine(MessageType.DIALOGUE, "Let's make some passwords!");
		}
		
		// launch the "main menu"
		int choice = mainMenu(gen);
		
		// repeat this process while the user doesn't want to quit
		while (choice != 3)
		{
			// determine what to do after the user chooses
			switch (choice)
			{
				case 0:		// chose "Make Passwords"
					promptPasswords(gen);
					break;
					
				case 1:		// chose "Password Settings"
					gen = passwordSettings(gen);
					break;
					
				case 2:		// chose "Personalize"
					ud = addToWordBank(ud);
					// set the generator's UserData and save the
					// UserData to a file
					gen.setUserData(ud);
					ud.saveUserData();
					break;
			}
			
			// get the choice again
			choice = mainMenu(gen);
		}
		
		// say goodbye! Program will close soon
		printConsoleLine(MessageType.STANDARD, "Goodbye!");
		
		//--------------------------------
		// close the scanner
		scanner.close();
	}
	
	/**
	 * Prints and handles user interaction with the program's
	 * "main menu"
	 * @param gen - the generator used to create passwords
	 */
	private static int mainMenu(Generator gen)
	{
		printConsoleLine(MessageType.PLAIN, "");
		printConsoleLine(MessageType.STANDARD, "What would you like to do?");
		printConsoleLine(MessageType.HEADER, "  1) Make Passwords");
		printConsoleLine(MessageType.HEADER, "  2) Password Settings");
		printConsoleLine(MessageType.HEADER, "  3) Personalize");
		printConsoleLine(MessageType.HEADER, "  4) Quit");
		
		// get the user's preference
		int responseIndex = -1;
		while (responseIndex < 0)
		{
			// get the user response
			String response = readLine().toLowerCase();
			
			// chose: MAKE PASSWORDS
			if (response.contains("1") || response.contains("make"))
			{ responseIndex = 0; }
			// chose: PASSWORD SETTINGS
			else if (response.contains("2") || response.contains("settings"))
			{ responseIndex = 1; }
			// chose: PERSONALIZE
			else if (response.contains("3") || response.contains("personalize"))
			{ responseIndex = 2; }
			// chose: QUIT
			else if (response.contains("4") || response.contains("personalize"))
			{ responseIndex = 3; }
			// didn't choose any of the options
			else
			{
				printConsoleLine(MessageType.ERROR, "Sorry, that's not a valid option.");
			}
		}
		
		return responseIndex;
	}
	
	// --------------- Input/UserData Modification -------------- //
	/**
	 * Reads a line of user input from the command line, using the
	 * scanner initialized in mainThread()
	 * @return the string that was read, or null, if an error occurred
	 */
	private static String readLine()
	{
		try
		{
			return scanner.nextLine();
		}
		catch (Exception e)
		{
			return null;
		}
		
	}
		
	/**
	 * Prompts the user to enter their name, to be saved
	 * into the UserData object.
	 * @return a string - the user's inputted name
	 */
	private static String promptUserName()
	{
		// print a little bit of intro dialogue
		printSnowman();
		printConsoleLine(MessageType.DIALOGUE, "I am... Dan. The password-making Snowman.");
		printConsoleLine(MessageType.DIALOGUE, "Ever since I was given this magic hat, I've been alive!");
		printConsoleLine(MessageType.DIALOGUE, "Strangely, my only desire is to create randomized passwords...");
		printConsoleLine(MessageType.PLAIN, "");
		printConsoleLine(MessageType.DIALOGUE, "What is YOUR name?");
		
		// open a scanner and read the name from the console
		String name = readLine();
		
		// make sure the name isn't null, and respond
		if (name != null)
		{ printConsoleLine(MessageType.DIALOGUE, "Nice to meet you " + name + "! Let's make some passwords."); }
		else
		{ printConsoleLine(MessageType.DIALOGUE, "Hmm, I couldn't read your name, for some reason."); }			
		
		return name;
	}
	
	/**
	 * Prompts the user various questions to create passwords
	 * @param gen - the generator to make passwords with
	 */
	private static void promptPasswords(Generator gen)
	{
		// read input for the number of passwords the user wants
		printConsoleLine(MessageType.STANDARD, "How many passwords do you want?");
		int count = -1;
		while (count <= 0)
		{
			// read user response
			String response = readLine().toLowerCase();
			
			// if the user wants to quit/go back, just return
			if (response.contains("back") || response.contains("quit"))
			{ return; }
			
			// otherwise, try to parse the integer from the response
			try
			{ count = Integer.parseInt(response); }
			catch (Exception e)
			{ printConsoleLine(MessageType.ERROR, "Please enter a number, or type \"back\""); }
		}
		
		// read input for a password length range
		printConsoleLine(MessageType.STANDARD, "Is there a specific range of length you want these passwords to be?");
		printConsoleLine(MessageType.STANDARD, "Enter the range like so: \"X-Y\" (you can also say \"no\")");
		int[] range = {-1, -1};
		boolean wantsRange = true;	// whether or not the user actually wants a length range
		while (wantsRange && (range[0] <= 0 || range[1] <= 0))
		{
			// read user response
			String response = readLine().toLowerCase();
			
			// if the user wants to quit/go back, just return
			if (response.contains("back") || response.contains("quit"))
			{ return; }
			
			// if the user says "no", then they don't want
			// a specific range, so exit the loop
			if (response.contains("no"))
			{ wantsRange = false; }
			else
			{
				// otherwise, try to parse integers from the response
				try
				{
					String[] bits = response.split("-");
					range[0] = Integer.parseInt(bits[0]);
					range[1] = Integer.parseInt(bits[1]);
					
					// if the range isn't correct, flip them!
					if (range[0] > range[1])
					{
						int temp = range[1];
						range[1] = range[0];
						range[0] = temp;
					}
				}
				catch (Exception e)
				{ printConsoleLine(MessageType.ERROR, "Please enter a range (\"X-Y\"), type \"no\", or type \"back\""); }				
			}
		}
		
		// if the range has been set, set the generator's range
		if (wantsRange)
		{
			printConsoleLine(MessageType.STANDARD,
					"Passwords of lengths between " + range[0] + " and " + range[1] + " characters will be created.");
			gen.setLengthRange(range[0], range[1]);
		}
		
		// create the passwords!
		printConsoleLine(MessageType.STANDARD, "Generating passwords...");
		printConsoleLine(MessageType.PLAIN, "");
		String[] passwords = gen.makePasswords(count);
		
		// print the passwords!
		printPasswords(passwords);
	}
	
	/**
	 * Prompts the user to adjust various settings regarding
	 * password generation
	 * @param gen - the generator to use for password generation
	 * @return returns the modified generator object
	 */
	private static Generator passwordSettings(Generator gen)
	{
		String response = "";
		// repeat this entire process until the user wants out
		while (!response.contains("back"))
		{
			// set up strings to indicate usages
			String numUsage = "OFF";
			if (gen.getUseNumbers())
			{ numUsage = "ON"; }
			
			String symUsage = "OFF";
			if (gen.getUseSymbols())
			{ symUsage = "ON"; }
			
			String undUsage = "OFF";
			if (gen.getUseUnderscores())
			{ undUsage = "ON"; }
			
			// print the menu
			printConsoleLine(MessageType.PLAIN, "");
			printConsoleLine(MessageType.STANDARD, "What would you like to adjust?");
			printConsoleLine(MessageType.HEADER, "  1) Toggle Number Usage       (Currently: " + numUsage + ")");
			printConsoleLine(MessageType.HEADER, "  2) Toggle Symbol Usage       (Currently: " + symUsage + ")");
			printConsoleLine(MessageType.HEADER, "  3) Toggle Underscore Usage   (Currently: " + undUsage + ")");
		
			// get user input
			int decision = -1;
			while (decision < 0)
			{
				// read the user response
				response = readLine().toLowerCase();
				
				// chose "back"
				if (response.contains("back"))
				{ decision = 0; }
				// chose "Toggle Number Usage"
				else if (response.contains("1") || response.contains("number"))
				{ decision = 1; }
				// chose "Toggle Symbol Usage"
				else if (response.contains("2") || response.contains("symbol"))
				{ decision = 2; }
				// chose "Toggle Underscore Usage"
				else if (response.contains("3") || response.contains("underscore"))
				{ decision = 3; }
				// didn't choose any of the valid responses
				else
				{
					printConsoleLine(MessageType.ERROR, "Sorry that's not a valid choice. (You can always say \"back\")");
				}			
			}
			
			// determine what to adjust with the decision
			switch (decision)
			{
				case 1:
					// flip the number usage preference
					gen.userWantsNumbers(!gen.getUseNumbers());
					// print out the result
					if (numUsage.equals("OFF"))
					{ printConsoleLine(MessageType.STANDARD, "Number usage is now ON"); }
					else
					{ printConsoleLine(MessageType.STANDARD, "Number usage is now OFF"); }
					break;
				
				case 2:
					// flip the symbol usage preference
					gen.userWantsSymbols(!gen.getUseSymbols());
					// print out the result
					if (symUsage.equals("OFF"))
					{ printConsoleLine(MessageType.STANDARD, "Symbol usage is now ON"); }
					else
					{ printConsoleLine(MessageType.STANDARD, "Symbol usage is now OFF"); }
					break;
					
				case 3:
					// flip the underscore usage prefrence
					gen.userWantsUnderscores(!gen.getUseUnderscores());
					// print out the result
					if (undUsage.equals("OFF"))
					{ printConsoleLine(MessageType.STANDARD, "Underscore usage is now ON"); }
					else
					{ printConsoleLine(MessageType.STANDARD, "Underscore usage is now OFF"); }
			}
			
		}
		
		// return the modified generator
		return gen;
	}
	
	/**
	 * Function that allows the user to add their favorite
	 * words to their personal word bank.
	 * @param user - the UserData object to modify
	 * @return the modified UserData object
	 */
	private static UserData addToWordBank(UserData user)
	{
		// print the introduction
		printSnowman();
		printConsoleLine(MessageType.DIALOGUE, "Do you want your passwords to be a bit more interesting?");
		printConsoleLine(MessageType.DIALOGUE, "You can tell me some of your favorite words here.");
		printConsoleLine(MessageType.DIALOGUE, "I'll remember them, and you'll see those words in future passwords!");
		printConsoleLine(MessageType.PLAIN, "");

		printConsoleLine(MessageType.STANDARD, "(You can say \"back\" to return to the main menu.)");
		printConsoleLine(MessageType.STANDARD, "(You can say \"view\" to see all the words in your word bank.)");
		printConsoleLine(MessageType.STANDARD, "(You can say \"clear\" to empty your word bank.");
		printConsoleLine(MessageType.PLAIN, "");
		
		// go through this entire process until the user
		// is done adding words to their word bank
		boolean done = false;
		while (!done)
		{
			// print the "header"
			printConsoleLine(MessageType.STANDARD, "Enter a word, and press enter to add it to your word bank.");
			
			// get user input
			String response = readLine().toLowerCase();
			
			// if the user wants out, exit the loop
			if (response.contains("back"))
			{ done = true; }			
			// if the user wants to view their words:
			else if (response.contains("view"))
			{
				printSnowman();
				String[] words = user.getFavWords();
				
				if (words.length > 0)
				{
					printConsoleLine(MessageType.DIALOGUE, "Here are all the words you've had me remember:");
					// iterate through each word and print it out
					for (int i = 0; i < words.length; i ++)
					{
						printConsoleLine(MessageType.PLAIN, words[i]);
					}
				}
				else
				{
					printConsoleLine(MessageType.DIALOGUE, "You haven't told me to remember any words!");
				}
			}
			// if the user wants to empty their word bank
			else if (response.contains("clear"))
			{
				// clear the words!
				user.clearWords();
				printConsoleLine(MessageType.STANDARD, "Your word bank has been cleared.");
			}
			// otherwise, add the word to the word bank
			// (as long as it's one word)
			else
			{
				if (!response.contains(" ") && !response.isEmpty())
				{
					// make sure the list already doesn't have this word
					if (!user.hasFavWord(response))
					{
						// add the word to the word bank
						user.addFavWord(response);
						// report that the word was added
						printConsoleLine(MessageType.STANDARD, "\"" + response + "\"" + " has been added to your word bank.");
					}
					else
					{ printConsoleLine(MessageType.STANDARD, "You already have this word in your word bank!"); }
				}
				else
				{
					printConsoleLine(MessageType.ERROR, "Make sure you only enter one word per line!");
				}
			}
			
			printConsoleLine(MessageType.PLAIN, "");
		}
		
		// return the modified UserData object
		return user;
	}
	
	// ------------ Command-Line Argument Handling ------------- //
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
	
	/**
	 * Method that checks for the user wanting to quickly generate a given
	 * number of passwords (this will happen only if the user enters the
	 * correct command-line argument)
	 * @param count - the number of passwords to generate
	 * @return a boolean indicating whether or not the quick generation
	 * 		   was completed
	 */
	private static boolean checkQuickGeneration(String[] args, Generator gen)
	{
		// look for the correct argument in args
		int quickIndex = findArgument(args, "quick");
		if (quickIndex >= 0)
		{
			// try to read the number of passwords to generate as the next argument
			try
			{
				int numPasswords = Integer.parseInt(args[quickIndex + 1]);				
				printConsoleLine(MessageType.STANDARD,
						"Quick-generation: generating " + numPasswords + " passwords...");
				printConsoleLine(MessageType.PLAIN, "");;
				
				// create the passwords and print them out
				printPasswords(gen.makePasswords(numPasswords));
				
				return true;
			}
			catch (Exception e)
			{
				printConsoleLine(MessageType.ERROR,
						"Quick-generation failed: Argument must be in the format: \"quick X\"");
				return false;
			}
		}
		
		return false;
	}
	
	/**
	 * Helper method that looks for user preferences in the arguments (such as turning
	 * numbers, symbols, or underscores on/off), applies them to the password generator,
	 * and returns it.
	 * @param args - the command-line arguments to read from
	 * @param gen - the password generator to modify
	 * @return the modified password generator
	 */
	private static Generator applyUserPreferences(String[] args, Generator gen)
	{
		// search for all 3 arguments
		String[] commands = {"numbers", "symbols", "underscores"};
		int[] indexes = {-1, -1, -1};
		boolean[] prefs = {false, false, false};
		String[] reports = {"Number usage is ", "Symbol usage is ", "Underscore usage is "};
		
		// iterate through all 3 commands, searching for command-line
		// arguments containing specified preferences for each
		for (int i = 0; i < commands.length; i++)
		{		
			// find the current index
			indexes[i] = findArgument(args, commands[i]);

			// only try to parse the preference input if the argument
			// was found in the command-line arguments
			if (indexes[i] >= 0)
			{
				// if the argument was found, try to get the "on/off" portion
				try
				{
					String preference = args[indexes[i] + 1];
					prefs[i] = preference.toLowerCase().equals("on");
	
					// apply the correct preference to the correct
					// generator property
					switch (i)
					{
						case 0:
							gen.userWantsNumbers(prefs[i]);
							break;
						case 1:
							gen.userWantsSymbols(prefs[i]);
							break;
						case 2:
							gen.userWantsUnderscores(prefs[i]);
							break;
					}
				}
				catch (Exception e)
				{
					printConsoleLine(MessageType.ERROR,
							"Exception occurred while setting user preferences on using " + commands[i]);
					printConsoleLine(MessageType.ERROR, 
							"Make sure each preference is formatted like so: \"<preference> <on/off>\"");
				}
			}
			
			// report the usage setting for each preference
			if (prefs[i])
			{ printConsoleLine(MessageType.STANDARD, reports[i] + "ON"); }
			else
			{ printConsoleLine(MessageType.STANDARD, reports[i] + "OFF"); }
			
		}
		
		// print a line break
		printConsoleLine(MessageType.PLAIN, "");;
		
		// return the modified generator
		return gen;
		
	}
	
	
	// ------------------- Printing Methods ------------------- //
	/**
	 * Helper function that prints a given string to the console, with a
	 * certain prefix or suffix attached to it.
	 * @param printMe - the string to print to the console
	 */
	private static void printConsoleLine(MessageType type, String printMe)
	{
		String prefix = "";
		String suffix = "";
		switch (type)
		{
			case STANDARD:
				prefix = "> ";
				break;
			case ERROR:
				prefix = "! ";
				break;
			case HEADER:
				prefix = "  ";
				break;
			case DIALOGUE:
				prefix = " \"";
				suffix = "\"";
				break;
			default:
				// do nothing - this was just to get
				// Eclipse to shut up
				break;
		}
		
		System.out.println(prefix + printMe + suffix);
	}
	
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
		// print a snowman with some snow
		printSnowmanWithSnow();
		printConsoleLine(MessageType.DIALOGUE, "Let it snow! (pretend those snowflakes are passwords)");
		printConsoleLine(MessageType.PLAIN, "");
		
		// print out each password on its own line
		for (int i = 0; i < passwords.length; i++)
		{
			printString(passwords[i] + "\n", null);
		}
	}
	
	/**
	 * Helper method that prints an ASCII snowman
	 */
	private static void printSnowman()
	{
		printConsoleLine(MessageType.HEADER, "   _==_ _");
		printConsoleLine(MessageType.HEADER, " _,(\",)|_|");
		printConsoleLine(MessageType.HEADER, "  \\/. \\-|");
		printConsoleLine(MessageType.HEADER, "__( :  )|_");
		
		// print a blank line
		printConsoleLine(MessageType.PLAIN, "");;
	}

	/**
	 * Helper method that prints some ASCII snow!
	 */
	private static void printSnowmanWithSnow()
	{
		printConsoleLine(MessageType.HEADER, "   _==_ _   *  .      .   *  *     .    *   *   .   *  .");
		printConsoleLine(MessageType.HEADER, " _,(\",)|_|    *  .   *    .  *  .    . *   .    .   *");
		printConsoleLine(MessageType.HEADER, "  \\/. \\-|   .   .    *   . .     *     *    *    * .   *");
		printConsoleLine(MessageType.HEADER, "__( :  )|_  *   .  *   *  .  * *   *   .  *   . .   * .");
		
		// print a blank line
		printConsoleLine(MessageType.PLAIN, "");;
	}
	
	/**
	 * Prints the main error message, if an error occurs
	 * while the program is running.
	 */
	private static void printErrorMessage()
	{
		// print a sad snowman (has a period for a mouth
		// instead of a comma, lol)
		printConsoleLine(MessageType.HEADER, "   _==_ _");
		printConsoleLine(MessageType.HEADER, " _,(\".)|_|");
		printConsoleLine(MessageType.HEADER, "  \\/. \\-|");
		printConsoleLine(MessageType.HEADER, "__( :  )|_");
		
		// print a blank line
		printConsoleLine(MessageType.PLAIN, "");;
		
		printConsoleLine(MessageType.DIALOGUE, "Hmm... something went wrong.");
		printConsoleLine(MessageType.PLAIN, "");
		printConsoleLine(MessageType.ERROR, "Make sure the following is true:");
		printConsoleLine(MessageType.ERROR, "    1) There's two folders in the same directory as the executable:");
		printConsoleLine(MessageType.ERROR, "       \"data_user\" and \"data_words\".");
		printConsoleLine(MessageType.ERROR, "    2) \"data_user\" contains a text file called \"userData.txt\".");
		printConsoleLine(MessageType.ERROR, "       (if one isn't there, you can create an empty one.)");
		printConsoleLine(MessageType.ERROR, "    3) \"data_words\" has all the \"words<0-9>.txt\" text files inside it.");
	}
	
}
