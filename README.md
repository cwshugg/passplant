# The "Snowflake Password Generator"

__Passwords should probably be like snowflakes...__ no two are alike! (unless of course, you're using this generator and you happen to get the same exact password with the same length, words, symbols, and numbers... not super likely, though.)

# What is this?
A password generator! This is a quick side project I whipped up. The idea is to create passwords that are both secure and memorable, not just a long string of digits and letters. Most of the words used are randomly pulled from word banks.

There's also an option that exists that allows you to log your "favorite words." Once you do so, you'll occasionally see some of those words in passwords the tool generates. Hopefully that should make for passwords that are a little more relevant to you and your interests.

# Why make it?
I'm not the best at giving my passwords variety, and while I've certainly tried to use the various services out there that store and save your passwords, I never really liked the passwords they recommended (something like: djz5214Jwhy23_31!?@az). Instead, I'd much rather prefer a password just as long, but something a little more memorable (like the well-known "correctHorseBatteryStaple" thanks to xkcd: https://xkcd.com/936/).

Thus, I decided to write a small password generator that asks you a few questions and uses your personal preferences to generate something long, secure, and most importantly, interesting.

CREDIT WHERE IT'S DUE:
I found a great public repo containing lots of commonly-used english words. I've used some of these to generate most words in the generator. Here it is: (https://github.com/first20hours/google-10000-english).
I also borrowed some ASCII art from this site: (https://www.asciiart.eu/holiday-and-events/christmas/snowmen)
Lastly, I used the the free software located at (http://www.jar2exe.com/) to convert the .jar file into an .exe file.

# Want to try it out?
Download the latest folder inside the "download" folder. Be sure to take a look at the README inside to get a good idea on how to use it.

# Future Plans
I'm hoping to implement the following features when I find some spare time:
**Plausible**
- Remembering last-used password settings from the last session
- Writing generated passwords to a file for easy copying
- Using a HTTP GET to ping a random word generator online (having the old file-reading technique as a backup when Internet isn't available). (Words would be stored in a buffer that refills itself whenever empty, to minimize GETs)
**Stretching it/slightly Far-fetched** 
- A password-protected storage area for past-used passwords (including notes with the password, such as what website it's for, a corresponding username, etc.)
- Themed word-generation based on nearby holidays/current events, or user-specified "theme files"