package Frontend;
import java.util.ArrayList;
import java.io.FileReader;
import java.io.IOException;

import Parser.*;
import Time.*;

public class Workflow {
    

    public static void main(String[] args) {
        Organizer organizer;
        try {
            FileReader fr = new FileReader("test.txt");
            Parser p = new Parser(fr);
            p.workflowTokenizer();
            ArrayList<String> tokens = p.getTokens();
            organizer = new Organizer(tokens);

            FrontendWorkflow testFrontendWorkflow = new FrontendWorkflow(organizer);
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    
}

class Test {
    public static void main1(String[] args) {
        try {
            FileReader fr = new FileReader("test.txt");
            Parser p = new Parser(fr);
            p.workflowTokenizer();
            ArrayList<String> tokens = p.getTokens();
            
            //Organizer organizer = new Organizer(tokens);
   
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
       
        
    }
}

class FrontendWorkflow{
    private ArrayList<Job> jobs;
    private ArrayList<Station> stations;
    private ArrayList<Task> tasks;

    Organizer organizer;
    public FrontendWorkflow(Organizer organizer){
        this.organizer = organizer;
        getArraysFromOrganizer(organizer);
    }
    public void getArraysFromOrganizer(Organizer organizer){
        jobs = organizer.getJobs();
        stations = organizer.getStations();
        tasks = organizer.getTasks();
    }
}