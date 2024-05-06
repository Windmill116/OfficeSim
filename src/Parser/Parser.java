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
    private LineNumberReader jobFileReader;
    private ArrayList<String> jobTokens;
    /* Gets argument as FileReader and */
    public Parser(FileReader r,FileReader jobFileReader){
        Locale.setDefault(Locale.ENGLISH);
        this.reader = new LineNumberReader(r);
        this.jobFileReader = new LineNumberReader(jobFileReader);
        this.currentDepth =0;
        this.tokens = new ArrayList<String>();
        this.jobTokens = new ArrayList<String>();
    }
    
    private String readLine(LineNumberReader reader){
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
    public void start()
    {
        workflowTokenizer();
        jobFileReader();
    }
    
    private int workflowTokenizer(){
        String line = readLine(this.reader);
        
        while(line != null){
            tokens.add(":line:");

            //Split line into pre-tokens
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
            line = readLine(this.reader);
        }
        // call jobreader
        return 0;
    }

    public ArrayList<String> getTokens() {
        
        
        
        this.tokens.removeIf(String::isBlank);
        tokens.add(":line:");
        return tokens;
    }

    public int getLine() {
        return reader.getLineNumber();
    }
    
    private int jobFileReader()
    {
        String line = readLine(jobFileReader);
        
        while(line != null){
            jobTokens.add(":line:");

            //Split line into pre-tokens
            String[] temp = line.split(" ");

            /*Add every token to general token list */
            for(String s: temp){
                StringBuilder tmp = new StringBuilder();
                
                /*Build a token char by char*/
                for(char s2: s.toCharArray()){

                    /*If char is (  or ) add another token */
                    if(!Character.isISOControl(s2))
                        tmp.append(s2);
                }

                jobTokens.add(tmp.toString().toLowerCase().trim());
                
            }
            line = readLine(jobFileReader);
        }
        
        return 0;
    }

    public ArrayList<String> getJobTokens() {
        return jobTokens;
    }

    public void setJobTokens(ArrayList<String> jobTokens) {
        this.jobTokens = jobTokens;
    }
    
    
    
    
}