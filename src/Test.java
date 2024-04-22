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
            Organizer organizer = new Organizer(tokens);
            
            for (Task task : organizer.getTasks()) {
                
                System.out.println(task.getName() + " " + task.getValue()) ; 
            }
            System.out.println("-----------------------------------------");
            for (Job job : organizer.getJobs()) {
                
                System.out.println(job.getName() + ": ") ; 
                
                for (Task task : job.getTasks()) {
                    
                   System.out.print(" " + task.getName() + " " + task.getValue()) ;
                    
                }
                System.out.println("");
                System.out.println("*********************************");
            }
            
            for (Station station : organizer.getStations()) {
                
                System.out.println(station.getName());
                
            }
            
            System.out.println("trimleri düzelt caneeer!!");

        
            
            
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
       
        
    }
}
