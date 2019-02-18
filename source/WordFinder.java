package passgen;

import java.io.File;
import java.nio.file.Paths;
import java.util.Scanner;
import java.util.Random;

/**
 * A class that deals entirely with generating random
 * words for the Generator class. This is accomplished
 * through multiple files containing hundreds of words.
 * 
 * @author Connor Shugg
 * @version 2019-2-15
 */
public class WordFinder
{
	private Random rand;				// reference to the Random class, for finding
										// random line numbers/words
	private UserData user;				// reference to a UserData object - to use
										// "favorite words" occasionally
	
	private String filePath;			// the path to the location of all the
										// word-files (files used to find random words
	private String filePrefix;			// the prefix at the front of all word files
	private int fileCount;				// the number of word-containing files the
										// generator can read from
	
	/**
	 * Constructs a WordFinder object and sets up
	 * the file information (path, prefix, number of
	 * files to choose from, etc.)
	 * @param ud - the UserData object to pull words from
	 */
	public WordFinder(UserData ud)
	{
		user = ud;
		
		// set up the random generator
		rand = new Random();
		
		// set up the file path/prefixes
		fileCount = 10;
		filePrefix = "words";
		filePath = Paths.get("").toAbsolutePath().toString()
				 + "\\data_words\\";
	}
	
	/**
	 * Randomly chooses between different methods of finding
	 * a random word, and carries it out, returning the word.
	 * @return a string - the random word that was found
	 */
	public String getRandomWord()
	{
		// randomly select a way to find a word to return
		int x = rand.nextInt(100);
		
		// CASE 1: pull from the User's "favorite word" database
		// (as long as the user HAS favorite words)
		if (x < 15 && user.getFavWords().length > 0)
		{
			String result = findUserWord();
			return result;
		}
		
		// DEFAULT CASE: use the normal strategy of searching
		// through random word files
		String result = findWord();
		return result;
	}
	
	
	// ----------------- Word-finding Methods ----------------- //
	/**
	 * "Standard" word-finding method. Makes use of the filePath,
	 * filePrefix, and fileCount to search a random file for a random
	 * word.
	 * @return a string - the random word found
	 */
	private String findWord()
	{
		// pick a random wordsX.txt file
		int fileNum = rand.nextInt(fileCount);
		File wordFile = new File(filePath + filePrefix + fileNum + ".txt");
		
		try
		{		
			// open a scanner on the file
			Scanner scan = new Scanner(wordFile);
			
			// each word file should be <= 1000 lines long,
			// so pick a random line number to travel to
			int line = rand.nextInt(1001);
			
			String result = "";
			for (int i = 0; i < line; i++)
			{
				if (scan.hasNextLine())
				{
					result = scan.nextLine();
				}
			}
			
			// close the scanner and return the resulting word
			// (converting it to lower-case)
			scan.close();			
			return result.toLowerCase();
		}
		catch (Exception e)
		{
			return "fail";
		}
	}
	
	/**
	 * Randomly selects from the UserData's "favorite words" to
	 * return in getRandomWord().
	 * @return a string - one of the user's favorite words
	 */
	private String findUserWord()
	{
		// retrieve the array of favorite words
		String[] words = user.getFavWords();
		
		// randomly select an index, and return the word
		// at that particular index (in lower-case letters)
		return words[rand.nextInt(words.length)].toLowerCase();
	}
	
}
