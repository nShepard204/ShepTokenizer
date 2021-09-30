// I/O Imports.
import java.io.StreamTokenizer;
import java.io.File;
import java.io.FileReader;
// Utility Imports.
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class ShepTokenizer {
    // Variable Initialization & Declarations.
    private StreamTokenizer tokens;
    private ShepToken currentTkn;
    private static final Map<String, Integer> symbols = populateSymbols();
    private boolean hasTokens = true;
    private static final boolean debugMode = false;
    private final Pattern enforceId = Pattern.compile("[A-Z_]+[0-9]+$|[A-Z]+");
    private char prevTkn = '\0';
    
    // Constructor.
    public ShepTokenizer(String fileName) {
        try {
            this.tokens = new StreamTokenizer(new FileReader(new File(fileName)));
        } catch (Exception e) {
            //TODO: handle exception
            System.err.println("An error occured trying to read the file: " + e.getMessage());
            System.exit(0);
        }
        // Add the additional words to the StreamTokenizer.
        populateTokenizer();
        skipToken();
    }

    // Accessor for hasTokens.
    public boolean hasTokens(){
        return hasTokens;
    }

    // Initializes the reserved keywords map.
    private static Map<String, Integer> populateSymbols(){
        Map<String, Integer> keywords = new HashMap<String, Integer>();
        Scanner keywordFile = null;
        try {
            keywordFile = new Scanner(new File("reservedKeywords.txt"));
        } catch (Exception e) {
            //TODO: handle exception
            System.err.println("An error occured trying to read the file: " + e.getMessage());
        }

        // Add common symbols and keywords into the Map.
        if(keywordFile != null){
            int id = 1;
            while(keywordFile.hasNextLine()){
                keywords.put(keywordFile.nextLine(), id);
                id++;
            }
        }
        /*
        for(Map.Entry<String, Integer> i : keywords.entrySet())
        {
            System.out.println("Token: " + i.getKey() + " - Symbol: " + i.getValue());
        }
        */

        return keywords;
    }

    // Sets the special tokens for the under the hood tokenizer.
    private void populateTokenizer(){
        for(Map.Entry<String, Integer> entry : symbols.entrySet()){
            if(this.tokens != null && entry.getKey().length() == 1 &&  entry.getValue() > 11 && entry.getValue() < 31){
                char newTkn = entry.getKey().charAt(0);
                tokens.ordinaryChar(newTkn);
            }
        }
    }

    // Returns the current token the tokenizer is looking at.
    public int getToken(){
        // Variable Initializations & Declarations.
        ShepToken tkn = new ShepToken();
        int intVal = 31, idVal = 32, eofVal = 33, retVal = 0;

        // Process Word Tokens.
        if(tokens.ttype == StreamTokenizer.TT_WORD){
            Matcher idTest = enforceId.matcher(tokens.sval);
            if(symbols.containsKey(tokens.sval)){
                tkn.setTokenVal(symbols.get(tokens.sval));
                retVal = symbols.get(tokens.sval);
            }
            // Process Identifier Tokens.
            else if(idTest.matches()){
                tkn.setTokenVal(idVal);
                tkn.setIdName(tokens.sval);
                retVal = idVal;
            }
            // Illegal Token Handler.
            else {
                System.err.println("Error! Illegal Token on Line " + tokens.lineno());
                System.exit(0);
            }
            tkn.setToken(tokens.sval);
        }
        // Process Number Tokens.
        else if(tokens.ttype == StreamTokenizer.TT_NUMBER){
            tkn.setToken("Integer");
            tkn.setTokenVal(intVal);
            tkn.setIntVal((int)tokens.nval);
            retVal = intVal;
        }
        // Process EOF Token.
        else if(tokens.ttype == StreamTokenizer.TT_EOF){
            tkn.setToken("EOF");
            tkn.setTokenVal(eofVal);
            hasTokens = false;
            retVal = eofVal;
        }
        // Process Special Tokens.
        else {
            // Get the token we're looking at and store it somewhere.
            this.prevTkn = (char)tokens.ttype;
            String prevTknStr = Character.toString(this.prevTkn);
            // Move onto the next token.
            this.skipToken();
            char nextTkn = (char)tokens.ttype;
            String nextTknStr = Character.toString(nextTkn);
            // If the two combined tokens creates one of our double char, return its value
            String doubleCharToken = prevTknStr + nextTknStr;
            if(symbols.containsKey(doubleCharToken)){
                retVal = symbols.get(doubleCharToken);
            }
            // If not, call pushBack and return prevTkn.
            else if(symbols.containsKey(prevTknStr)){
                tokens.pushBack();
                retVal = symbols.get(prevTknStr);
            }
            else {
                this.skipToken();
                retVal = this.getToken();
            }
        }
        this.currentTkn = tkn;
        return retVal;
    }

    // Skips the current token and moves onto the next token.
    public void skipToken(){
        try {
            tokens.nextToken();
        } catch (Exception e) {
            System.err.println("An error occured at skipToken(): " + e.getMessage());
        }
    }

    // Returns the value of the current integer token.
    public int intVal(){
        int retVal = 0;
        if(currentTkn.getIntVal() != null){
            retVal = currentTkn.getIntVal();
        }
        else {
            if(debugMode){
                retVal = -1;
            } 
            else{
                System.err.println("Error! Tried to get integer value of a non-integer token.");
                System.exit(0);
            }
        }
        return retVal;
    }

    // Returns the name of the idenifier the tokenizer's currently at.
    public String idName(){
        String name = null;
        if(currentTkn.getIdName() != null){
            Matcher idTest = enforceId.matcher(currentTkn.getIdName());
            if(idTest.matches()){
                name = currentTkn.getIdName();
            }
            else{
                if(debugMode){
                    name = "Not a valid ID!";
                } 
                else{
                    System.err.println("Error! Not a valid ID.");
                    System.exit(0);
                }
            }
        }
        else {
            if(debugMode){
                name = "not an id";
            } 
            else{
                System.err.println("Error! Tried to get ID of a non-ID token.");
                System.exit(0);
            }
        }
        return name;
    }

    public static void main(String[] args) {
        // Variable Initializations & Declarations.
        
        // Read file contents from cmd line.
        ShepTokenizer tokenizer = new ShepTokenizer(args[0]);

        System.out.print("Tokens: ");
        while(tokenizer.hasTokens()){
            int token = tokenizer.getToken();
            /* Info-Rich Output.
            System.out.println("Token Name: " + token.getToken());
            System.out.println("Token Value: " + token.getTokenVal());
            System.out.println("Current Token IntVal: " + tokenizer.intVal());
            System.out.println("Current Token ID Name: " + tokenizer.idName() + "\n");
            */

            // The output the assignment wants.
            System.out.println(token + " ");
            tokenizer.skipToken();
        }
    }
}