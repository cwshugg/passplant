package passgen;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.nio.file.Paths;
import java.util.Scanner;
import java.util.ArrayList;

/**
 * A class representing the User's Data for the Password Generator.
 * This will store information such as the user's name, preferences
 * and some other odds and ends that the generator can pull from to
 * create more "personal" passwords.
 * 
 * @author Connor Shugg
 * @version 2019-2-15
 */
public class UserData
{
	private String filePath;
	private String fileName;
	
	private String userName;
	private int favNumber;
	private String favColor;
	private ArrayList<String> favWords;
	
	
	/**
	 * Default constructor: Creates the UserData object,
	 * and runs a method that searches for a save-file
	 * to read previously-saved data from. If the file
	 * isn't found, an "empty" UserData object is created.
	 */
	public UserData()
	{
		// create default UserData setup
		userName = null;
		favNumber = 0;
		favColor = null;
		favWords = new ArrayList<String>();
		
		// set up the file path/name for the userData file
		fileName = "userData.txt";
		filePath = Paths.get("").toAbsolutePath().toString()
				 + "\\src\\" + fileName;
		
		// search for a save-file
		loadUserData();
	}
	
	
	// -------------------- Getter Methods -------------------- //
	/**
	 * Getter method for the user's name
	 * @return a string - the user's name
	 */
	public String getUserName()
	{
		return userName;
	}

	/**
	 * Getter method for the user's favorite number
	 * @return an integer - the user's favorite number
	 */
	public int getFavNumber()
	{
		return favNumber;
	}
	
	/**
	 * Getter method for the user's favorite color
	 * @return a string - the user's favorite color
	 */
	public String getFavColor()
	{
		return favColor;
	}
	
	/**
	 * Getter method for the user's favorite words.
	 * @return an array of strings - the user's favorite words
	 */
	public String[] getFavWords()
	{
		return favWords.toArray(new String[favWords.size()]);
	}
	
	
	// -------------------- Setter Methods -------------------- //
	/**
	 * Setter method for the user's name
	 * @param name - the new userName
	 */
	public void setUserName(String name)
	{
		userName = name;
	}
	
	/**
	 * Setter method for the user's favorite number
	 * @param num - the new favorite number
	 */
	public void setFavNumber(int num)
	{
		favNumber = num;
	}
	
	/**
	 * Setter method for the user's favorite color
	 * @param color - the new favorite color
	 */
	public void setFavColor(String color)
	{
		favColor = color;
	}
	
	/**
	 * Adds a new word to the user's favorite word list
	 * @param word - the new word to add to the list
	 */
	public void addFavWord(String word)
	{
		favWords.add(word);
	}

	
	// ------------------ Loading and Saving ------------------ //
	/**
	 * Searches through the current directory for a userData
	 * file, returning a pointer reference said file, if found.
	 * Otherwise, null is returned
	 * @return - a reference to the file, or null
	 */
	private File findUserFile()
	{
		File userFile = new File(filePath);
		
		// if the file found is indeed a FILE, return it
		if (userFile.isFile())
		{
			return userFile;
		}
		
		// otherwise, the file must not have been found
		return null;
	}
	
	/**
	 * Searches around for a specifically-named file
	 * containing saved UserData. If it's found, the
	 * file is loaded in and the data is restored.
	 */
	private void loadUserData()
	{
		// try to locate the file
		File userFile = findUserFile();
		
		// if the file can be read from, read it!
		// otherwise, the function will just exit.
		try
		{
			Scanner scan = new Scanner(userFile);
			
			// read through the file and assign the
			// data read to the UserData's fields
			while (scan.hasNextLine())
			{
				// read line + split into comma-delimited parts
				String[] pieces = scan.nextLine().split(",");
				
				// setting userName
				if (pieces[0].equals("name"))
				{
					userName = pieces[1];
				}
			}
			
			// close the scanner
			scan.close();
		}
		catch (FileNotFoundException e) {
			// do nothing... if the file isn't found
			// or can't be read, no big deal, just
			// build this UserData object from the
			// ground up, and save it later
		}
	}
	
	/**
	 * Searches for the userData file. If it's not found,
	 * a file is created at the filePath, and the class's
	 * fields are written to the file
	 */
	public void saveUserData()
	{
		// look for the file before doing anything
		File userFile = findUserFile();
		
		// if it's not null, then a file was found
		if (userFile != null)
		{
			// write each property to the file
			try
			{
				// open a PrintWriter object
				PrintWriter writer = new PrintWriter(filePath, "UTF-8");
				
				// write userName, favorite number, and color
				writer.println("name," + userName);
				writer.println("favNumber," + favNumber);
				writer.println("favColor," + favColor);
				
				// write each favorite word
				writer.print("favWords,");
				String[] words = getFavWords();
				for (int i = 0; i < words.length - 1; i++)
				{
					writer.print(words[i] + ",");
				}
				writer.print(words[words.length - 1] + "\n");
				
				// close the writer
				writer.close();
			}
			// if something fails, catch it!
			catch (Exception e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
