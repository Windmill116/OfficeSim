
import Parser.JobType;
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
            FileReader jb = new FileReader("job.txt");
            Parser p = new Parser(fr,jb);// add job file to fix problem
            p.start();
            ArrayList<String> tokens = p.getTokens();
            ArrayList<String> jobTokens =  p.getJobTokens();
            Organizer organizer = new Organizer(tokens,p.getLine(),p.getJobTokens());
            
            
            
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
       
        
    }
}