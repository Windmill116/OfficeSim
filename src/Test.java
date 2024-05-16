
import Parser.Job;
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
        FileReader fr = null;
        FileReader jb = null;
        if(args.length < 2){
            System.out.println("USAGE: officesim.jar <workflow file> <job file>");
            return;
        }
        try {
             fr = new FileReader(args[0]);
             jb = new FileReader(args[1]);
            
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
            
            Parser p = new Parser(fr,jb);// add job file to fix problem
            p.start();
            ArrayList<String> tokens = p.getTokens();
            ArrayList<String> jobTokens =  p.getJobTokens();
            
            Organizer organizer = new Organizer(tokens, p.getLine(), jobTokens);
            
        for (Job job : organizer.getJobs()) {
            
            for (Task object : job.getTasks()) {
                
                System.out.println(object.getValue());
                
            }
            
        }
       
        
    }
}