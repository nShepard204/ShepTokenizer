// I/O Imports.
import java.io.StreamTokenizer;
import java.io.File;
import java.io.FileReader;
// Utility Imports.
import java.util.*;

class ShepTokenizer {
    // Variable Initialization & Declarations.
    private StreamTokenizer tknStream = null;
    private static final int QUOTE_CHARACTER = '\'';
    private static final int DOUBLE_QUOTE_CHARACTER = '"';
    private static final Map<Integer, String> reservedWords = createKeywordsMap();

    // Initialize Reserved Keywords Map


    public ShepTokenizer(String fileName) {
        try {
            StreamTokenizer tokenStream = new StreamTokenizer(new FileReader(new File(fileName)));
            this.tknStream = tokenStream;
            skipToken();
        } catch (Exception e) {
            //TODO: handle exception
            System.err.println("An error occured trying to read the file: " + e.getMessage());
        }
    }

    public void getToken()
    {
        if(tknStream != null)
        {
            try {
                if(tknStream.ttype == StreamTokenizer.TT_NUMBER)
                {
                    System.out.println("Number: " + intVal());
                }
                else if(tknStream.ttype == StreamTokenizer.TT_WORD || tknStream.ttype == QUOTE_CHARACTER || tknStream.ttype == DOUBLE_QUOTE_CHARACTER)
                {
                    System.out.println("Word: " + reservedWords.get(tknStream.nval));
                }
                else {
                    System.out.println("Current Token Value:" +  intVal());
                    System.out.println("Current Token ID:" +  idName());
                }
            } catch (Exception e) {
                System.err.println("An error occured at getToken(): " + e.getMessage());
            }
        }
    }
    public void skipToken()
    {
        if(tknStream != null)
        {
            try {
                tknStream.nextToken();
            } catch (Exception e) {
                System.err.println("An error occured at skipToken(): " + e.getMessage());
            }
        }
    }

    private int intVal(){
        int retVal = 0;
        retVal = (int)tknStream.nval;
        return retVal;
    }

    private String idName(){
        String name = "";
        name = tknStream.sval;
        return name;
    }

    private static Map<Integer, String> createKeywordsMap(){
        Map<Integer, String> keywords = new HashMap<Integer, String>();
        Scanner keywordFile = null;
        try {
            keywordFile = new Scanner(new File("reservedKeywords.txt"));
        } catch (Exception e) {
            //TODO: handle exception
            System.err.println("An error occured trying to read the file: " + e.getMessage());
        }

        if(keywordFile != null){
            int id = 1;
            while(keywordFile.hasNextLine()){
                keywords.put(id, keywordFile.nextLine());
                id++;
            }
        }

        /*
        for(Map.Entry<Integer, String> i : keywords.entrySet())
        {
            System.out.println("Token: " + i.getKey() + " - Symbol: " + i.getValue());
        }
        */

        return keywords;
    }

    public static void main(String[] args) {
        // Variable Initializations & Declarations.
        
        // Read file contents from cmd line.
        ShepTokenizer shepTokenizer = new ShepTokenizer(args[0]);
        shepTokenizer.getToken();
    }
}