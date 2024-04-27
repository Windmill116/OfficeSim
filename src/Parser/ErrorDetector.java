
package Parser;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ErrorDetector {
    
    String text;
    ArrayList<String> tokens;
    static int  line ;

    public ErrorDetector(String text) {
        this.text = text;
        
    }

    public ErrorDetector(ArrayList<String> tokens) {
        this.tokens = tokens;
    }
    
    public void findErrors()
    {
        TaskErrorDetector();
    }
    
    
    private void TaskErrorDetector()
    {
       if(!tokens.get(0).equals("("))
       {
           System.out.println(line + "( is missing");
       }
        for (int i = 2; i < tokens.size(); i++) {
            
            
            if(tokens.get(i).equals("jobtypes"))
            {
                if(!tokens.get(i-1).equals("("))
                {
                    System.out.println(line + "( is missing");
                 
                }
                if(!tokens.get(i-2).equals(")"))
                {
                   System.out.println(line + ") is missing"); 
                }
                break;
            }
                
            
            
            
                try {
                            
                     float val = (float)Double.parseDouble(tokens.get(i+1));
                     if(val<0)
                     {
                         System.out.println(line+ " " + tokens.get(i) + " value of task is smaller than 0 ");
                     }
                     i++;
                        
                    } catch (NumberFormatException e) {
                        
                       
                    }
                
            
        }
    }
}
