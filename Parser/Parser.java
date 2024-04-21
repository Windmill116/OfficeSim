package Parser;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.util.ArrayList;
import java.util.Locale;

public class Parser{
    private int currentDepth;
    private LineNumberReader reader;
    private ArrayList<String> tokens;
    /* Gets argument as FileReader and */
    public Parser(FileReader r){
        Locale.setDefault(Locale.ENGLISH);
        this.reader = new LineNumberReader(r);
        this.currentDepth =0;
        this.tokens = new ArrayList<String>();
    }
    
    private String readLine(){
        String line = null;
        try {
            if(reader.ready())
                line = reader.readLine();
            else
                return null;
            
        } catch (IOException e) {
            System.out.println("Couldn't read line");
        }
        
        return line;
    }


    public int workflowTokenizer(){
        String line = readLine();
        while(line != null){
            
            String[] temp = line.split(" ");

            /*Add every token to general token list */
            for(String s: temp){
                StringBuilder tmp = new StringBuilder();
                
                /*Build a token char by char*/
                for(char s2: s.toCharArray()){

                    /*If char is (  or ) add another token */
                    if(s2 == '('){
                        tokens.add("(");
                        continue;
                    }else if(s2 == ')'){
                        currentDepth++;
                        continue;
                    }
                    tmp.append(s2);
                }

                tokens.add(tmp.toString().toLowerCase().trim());
                for(int i=0;i<currentDepth;i++){
                    tokens.add(")");
                }
                currentDepth =0;
            }
            line = readLine();
        }
        return 0;
    }

    public ArrayList<String> getTokens() {
        return tokens;
    }



}