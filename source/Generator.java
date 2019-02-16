package passgen;

import java.io.File;
import java.nio.file.Paths;
import java.util.Random;
import java.util.Scanner;

/**
 * This class serves as the brains of the entire operation.
 * Holds a reference to UserData, and uses it, along with
 * any input given to generate passwords.
 * 
 * @author Connor Shugg
 * @version 2019-2-15
 */
public class Generator
{
	private UserData user;				// Reference to UserData object
	private WordFinder finder;			// Reference to a WordFinder object - for
										// retrieving random words
	private Random rand;				// Random reference used to generate random
										// numbers, word, etc.
	
	private boolean useUnderscores;		// whether or not to generate passwords
										// containing underscores
	private boolean useSymbols;			// whether or not to generate passwords
										// containing symbols
	private boolean useNumbers;			// whether or not to generate passwords
										// containing numbers
	private int[] lengthRange;			// the range determining how long to
										// make each password
	
	/**
	 * Default constructor: Initializes the UserData class,
	 * so that previously-saved data is read and stored.
	 */
	public Generator()
	{
		user = new UserData();
		finder = new WordFinder(user);
		rand = new Random();
		
		// set up default preferences
		useUnderscores = false;
		useSymbols = false;
		useNumbers = false;
		
		lengthRange = new int[2];
		lengthRange[0] = 12;
		lengthRange[1] = 24;
	}
	
	// -------------------- Setter Methods -------------------- //
	/**
	 * Sets the indicator boolean on whether or not to
	 * use underscores to generate passwords
	 * @param preference - the user's preference (yes or no)
	 */
	public void userWantsUnderscores(boolean preference)
	{
		useUnderscores = preference;
	}
	
	/**
	 * Sets the indicator boolean on whether or not to
	 * use symbols to generate passwords
	 * @param preference - the user's preference (yes or no)
	 */
	public void userWantsSymbols(boolean preference)
	{
		useSymbols = preference;
	}
	
	/**
	 * Sets the indicator boolean on whether or not to
	 * use numbers to generate passwords
	 * @param preference - the user's preference (yes or no)
	 */
	public void userWantsNumbers(boolean preference)
	{
		useNumbers = preference;
	}
	
	/**
	 * Sets the range of the length the user wants for
	 * their passwords
	 * @param lowerBound - the lower bound of the range
	 * @param upperBound - the upper bound of the range
	 */
	public void setLengthRange(int lowerBound, int upperBound)
	{
		// make sure the lower bound is indeed lower than the upper.
		// if that's not the case, swap them!
		if (lowerBound > upperBound)
		{
			int temp = upperBound;
			upperBound = lowerBound;
			lowerBound = temp;
		}
		
		// set the generator's range
		lengthRange[0] = lowerBound;
		lengthRange[1] = upperBound;
	}
	
	
	// ----------------- Password Generation ------------------ //
	/**
	 * Generates 'count' number of passwords, returning them as
	 * an array of strings.
	 * @param count - the number of passwords to generate
	 * @return an array of strings, containing the created passwords
	 */
	public String[] makePasswords(int count)
	{
		// just in case the user decides to generate zero passwords...
		if (count == 0)
		{ return null; }
		
		// otherwise.. get on with it!
		String[] passwords = new String[count];
		
		// perform the same password-creation process for
		// every password
		for (int i = 0; i < count; i++)
		{
			// first, determine a random length for the password
			// (based inclusively on both ends of the range)
			int length = rand.nextInt(lengthRange[1] - lengthRange[0] + 1) + lengthRange[0];
			
			passwords[i] = "";
			int wordCount = 0;
			
			// append random words (with numbers, symbols, or underscores
			// in between) one at a time, until the desired length is reached
			while (passwords[i].length() < length)
			{
				String word = finder.getRandomWord();
				
				// if the loop is on the second+ word,
				// AND the user doesn't want underscores,
				// make the first letter of the word upper-case
				if (wordCount > 0 && !useUnderscores && !word.equals(""))
				{
					String firstLetter = String.valueOf(word.charAt(0));
					firstLetter = firstLetter.toUpperCase();
					word = firstLetter + word.substring(1, word.length());
				}
				
				// if adding the word will go over the upper bound for the
				// password's length, don't add anything else to the password
				if (passwords[i].length() + word.length() <= lengthRange[1])				
				{
					// ONLY add the password to the word if the word isn't
					// already IN the password
					if (!passwords[i].contains(word))
					{
						// add the word to the password
						passwords[i] += word;
						wordCount++;
					
						// add some filler in-between 				
						passwords[i] += makeFiller();
					}
				}
			}
			
			// trim the password a little before moving
			// onto the next one
			passwords[i] = trimPassword(passwords[i]);
		}
		
		// return the created passwords
		return passwords;
	}
	
	/**
	 * Helper function for makePasswords that generates a small
	 * string of symbols, numbers, and underscores to fill the
	 * space in between words in a password
	 * @return a string - the filler generated
	 */
	private String makeFiller()
	{
		String filler = "";
		// UNDERSCORE PLACEMENT: Use a random integer to determine
		// whether to place the underscore after the word, or after
		// "nextPiece".
		int underscorePosition = rand.nextInt(100);
		if (useUnderscores && underscorePosition < 50)
		{
			filler += "_";
		}
		
		// insert a random symbol or number (if the user wants it)
		if (useNumbers && rand.nextInt(100) < 35)
		{
			filler += rand.nextInt(100);
		}
		if (useSymbols && rand.nextInt(100) < 35)
		{
			filler += getRandomSymbol();
		}				
		
		// UNDERSCORE PLACEMENT: If the underscore wasn't placed above
		// (before) "nextPiece", place it here
		if (useUnderscores && underscorePosition >= 50)
		{
			filler += "_";
		}
		
		return filler;
	}
	
	/**
	 * Helper function that trims off some select pieces from
	 * the end of the password (such as underscores)
	 * @param password - the password to modify
	 * @param desiredLength - the length the password SHOULD be
	 * @return a string - the modified password
	 */
	private String trimPassword(String password)
	{		
		// if the last character is an underscore, remove it
		if (password.charAt(password.length() - 1) == '_')
		{
			password = password.substring(0, password.length() - 1);
		}
		
		// return the modified password
		return password;
	}
	
	/**
	 * Generates and returns a random symbol.
	 * @return a string containing a single symbol
	 */
	private String getRandomSymbol()
	{
		// source I used to get the symbols:
		// https://www.owasp.org/index.php/Password_special_characters
		
		//String[] symbols = {"!", "\"", "#", "$", "%", "&", "'", "(",
		//					")", "*", "+", ",", "-", ".", "/", ":",
		//					";", "<", "=", ">", "?", "@", "[", "\\",
		//					"]", "^", "", "`", "{", "|", "}", "~"};
		
		String[] symbols = {"!", "#", "$", "%", "&", ".", ",", "-", "@",
							"(", ")", ":", ";", "<", ">", "?", "*", "~"};
		
		// generate a random index and return that
		// particular symbol
		return symbols[rand.nextInt(symbols.length)];
	}
	
	
}
