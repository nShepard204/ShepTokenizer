Welcome to the ShepTokenizer!

The list of files that are REQUIRED for ShepTokenizer to operate properly are: 
    ShepTokenizer.java - Contains the Tokenizer implementation.
    ShepToken.java - A supplementary class that's used for the underlying implementation of the Tokenizer.
    reservedKeywords.txt - Basically contains every special word or symbol that CORE programs care about. Needed for the token logic.

How to compile ShepTokenizer on CSE Lab Computers (Windows):
    So, for some reason it was impossible for me to get Java on the Universities computers and try my code out there, so instead I'll tell you
    how I did it with my own Windows laptop.

    I developed the entire thing with VSCode. I downloaded the Coding Pack provided by VSCode (https://aka.ms/vscode-java-installer-win). To
    compile the program, I opened a terminal in VSCode and typed: "javac ShepTokenizer.java ShepToken.java" which generated the .class files.

How to run ShepTokenizer on CSE Lab Computers (Windows):
    After compiling ShepTokenizer successfully, all you need to do to run it is open another terminal in VSCode and type: "java ShepTokenizer"
    and then include the path of the file you want to tokenize. The lab description said that the file path would be a command-line argument,
    so thats how my program gets the file path.

If you have any questions regarding anything about ShepTokenizer, please email me at shepard.204@osu.edu