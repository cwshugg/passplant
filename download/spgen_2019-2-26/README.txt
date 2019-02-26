Hi! This is the README for the "Snowflake Password Generator" tool. It should be pretty short,
so here we go:

Version: 2019-2-18

// ---------------------------------------- Running ---------------------------------------- //
-To run the program normally, just double-click the "spgen.exe" file.
-If for some reason this isn't working, and you have java installed, you can open a
 Command Prompt at the file's location, and run the command: "java -jar spgen.jar"

 
// -------------------------- Running with Command-Line Arguments -------------------------- //
-If you're feeling fancy, there are some command-line arguments you can supply
 if you're running it from the Command Prompt. They can be added like so:
 
    "spgen.exe quick <X>"               This skips the main menu and stuff that some might
                                        call "time-wasting," and immediately generates X
                                        number of passwords.
        
    "spgen.exe numbers <on/off>         This forces the generator to include numbers in
                                        the passwords it generates.
                                        
    "spgen.exe symbols <on/off>         This forces the generator to include symbols in
                                        the passwords it generates.
                                    
    "spgen.exe underscores <on/off>     This forces the generate to place underscores in
                                        between the words that make up each generated
                                        password - snake-case, if you will. (Without
                                        underscores, the words are set up in camel-case.)
                                        
-All of these command-line arguments can be included individually, or all together at once.


// ----------------------------- What are those data folders for? -------------------------- //
-"data_user" holds a small amount of information on the user (example: your name). Emptying
 the "userData.txt" file will reset your User Data.
-"data_words" holds several files containing random english words (each being 1000 lines long,
 or shorter). You're welcome to change these, but make sure they don't go over 1000 lines. If
 they do, the words on the lines above 1000 will not be used to generate passwords. (Also:
 keep it at one word per line, and keep the file names the same.)

