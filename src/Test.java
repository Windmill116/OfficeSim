import Parser.ErrorDetector;
import Parser.Job;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import Parser.Organizer;
import Parser.Parser;
import Parser.Station;
import Parser.Task;

public class Test {
    public static void main(String[] args) {
        try {
            FileReader fr = new FileReader("test.txt");
            Parser p = new Parser(fr);
            p.workflowTokenizer();
            ArrayList<String> tokens = p.getTokens();
            for (String token : tokens) {
                System.out.println(token);
            }
            ErrorDetector errorDetector = new ErrorDetector(tokens);
            errorDetector.findErrors();
            
            Organizer organizer = new Organizer(tokens);
            
            
            
            
        
            
            
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
       
        
    }
}