// I/O Imports.
import java.io.StreamTokenizer;
import java.io.File;
import java.io.FileReader;
// Utility Imports.

class ShepTokenizer {
    // Variable Initialization & Declarations.
    private StreamTokenizer tknStream = null;
    private static final int QUOTE_CHARACTER = '\'';
    private static final int DOUBLE_QUOTE_CHARACTER = '"';

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
                    System.out.println("Word: " + idName());
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

    public static void main(String[] args) {
        // Variable Initializations & Declarations.
        
        // Read file contents from cmd line.
        ShepTokenizer shepTokenizer = new ShepTokenizer(args[0]);
        shepTokenizer.getToken();
    }
}
