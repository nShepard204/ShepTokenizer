public class Main {
    public static void main(String[] args){
        try {
            Tokenizer t = new Tokenizer(args[0]);
            int currToken = t.getToken();
            while(currToken < 33){
                System.out.println(currToken);
                t.skipToken();
                currToken = t.getToken();
                if(currToken == 33){
                    System.out.println("Reached EOF, printed 33");
                }
                if(currToken > 33){
                    System.err.println("Got Token Error");
                }
            }
        } catch (Exception e) {
            System.err.println(e);
        }
    }
}
