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
            organizer = new Organizer(tokens,p.getLine());

            FrontendWorkflow testFrontendWorkflow = new FrontendWorkflow(organizer);
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    
}

class FrontendWorkflow{
    private ArrayList<Job> jobs;
    private ArrayList<Station> stations;
    private ArrayList<Task> tasks;

    private ArrayList<tempJob> testJobs;

    Organizer organizer;
    public FrontendWorkflow(Organizer organizer){
        this.organizer = organizer;
        getArraysFromOrganizer(organizer);
        createTestJobs();
    }
    public void getArraysFromOrganizer(Organizer organizer){
        jobs = organizer.getJobs();
        stations = organizer.getStations();
        tasks = organizer.getTasks();
    }

    public void createTestJobs(){
        /*Job1 J1 1 30
        Job2 J1 2 29
        Job3 J2 5 40
        Job4 J2 7 35
        Job5 J3 10 30 */

        testJobs = new ArrayList<>();
        testJobs.add(new tempJob("J1",0,30));
        testJobs.add(new tempJob("J1",2,29));
        testJobs.add(new tempJob("J2",5,40));
        testJobs.add(new tempJob("J2",7,35));
        testJobs.add(new tempJob("J3",10,30));
    }
}

class tempJob{
    Job jobType; //to be replaced with JobType
    ArrayList<Task> tasks;
    double startTime;
    double duration;

    public tempJob(String name,double startTime,double duration){
        Job jobType = new Job(name);
        ArrayList<Task> tasks = new ArrayList<>();
        this.startTime = startTime;
        this.duration = duration;
    }

    public void addTask(Task t){
        tasks.add(t);
    }

    public Job getJobType() {
        return jobType;
    }

    public void setJobType(Job jobType) {
        this.jobType = jobType;
    }

    public ArrayList<Task> getTasks() {
        return tasks;
    }

    public void setTasks(ArrayList<Task> tasks) {
        this.tasks = tasks;
    }

    public double getStartTime() {
        return startTime;
    }

    public void setStartTime(double startTime) {
        this.startTime = startTime;
    }

    public double getDuration() {
        return duration;
    }

    public void setDuration(double duration) {
        this.duration = duration;
    }

}