
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
            
            Organizer organizer = new Organizer(tokens,p.getLine());
            
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
       
        
    }
}