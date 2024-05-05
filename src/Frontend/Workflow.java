package Frontend;
import java.util.*;

import javax.swing.event.DocumentEvent.EventType;

import java.io.FileReader;
import java.io.IOException;

import Parser.*;
import Time.*;

/*NOTES
 * 1- I will assume the the jobStartTime is HOUR and the duration is in MINUTES
 * 2- I will assume that the Jobs go one by one and tasks arent mixed but rather done Job by Job.
 * 3- I will assume that the Job will select the station with the least job first.
 */


public class Workflow {
    static boolean testMode = true;
    static enum EventType{
        MESSAGE,REMOVE_TASK,QUEUE_TASK,ADD_TASK,FINISH_JOB,DO_JOB;
    }
    public static void main(String[] args) {
        FrontendWorkflow testFrontendWorkflow;
        if(!testMode) {
            Organizer organizer;
            try {
                FileReader jb = new FileReader("job.txt");
                FileReader fr = new FileReader("test.txt");
                Parser p = new Parser(fr,jb);// add job file to fix problem
                p.start();
                ArrayList<String> tokens = p.getTokens();
                organizer = new Organizer(tokens,p.getLine(),p.getJobTokens());

                testFrontendWorkflow = new FrontendWorkflow(organizer);
                
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else testFrontendWorkflow = new FrontendWorkflow();
    }

    
}

class FrontendWorkflow{

    private ArrayList<tempJob> jobs;
    private ArrayList<Station> stations;
    private ArrayList<Task> tasks;

    ArrayList<tempJob> testJobs;
    ArrayList<Task> testTasks;
    ArrayList<JobType> testJobTypes;
    ArrayList<Station> testStations;

    Organizer organizer;
    public FrontendWorkflow(Organizer organizer){
        this.organizer = organizer;
        getArraysFromOrganizer(organizer);
    }

    public FrontendWorkflow(){
        createTestObjects();
        assignTestObjectsAsMain();
        System.out.println(getTheFreeStation(jobs.get(2)).getName());
        
        

    }
    public void getArraysFromOrganizer(Organizer organizer){
        //jobs = organizer.getJobs();
        stations = organizer.getStations();
        tasks = organizer.getTasks();
    }
    public void createTestObjects(){
        /*Job1 J1 1 30
        Job2 J1 2 29
        Job3 J2 5 40
        Job4 J2 7 35
        Job5 J3 10 30 */

        testTasks= new ArrayList<>();
        testTasks.add(new Task("T1",1));
        testTasks.add(new Task("T2",2));
        testTasks.add(new Task("T3",2.5f));
        testTasks.add(new Task("T5",4));
        testTasks.add(new Task("T_1",5));
        testTasks.add(new Task("T21", 5));

        testJobTypes = new ArrayList<>();
        JobType J1 = new JobType("J1");
        ArrayList<Task> J1Tasks = new ArrayList<>();
        J1Tasks.add(testTasks.get(0));
        J1Tasks.add(testTasks.get(1));
        J1Tasks.add(testTasks.get(2));
        JobType J2 = new JobType("J2");
        ArrayList<Task> J2Tasks = new ArrayList<>();
        J2Tasks.add(testTasks.get(1));
        J2Tasks.add(testTasks.get(2));
        J2Tasks.add(testTasks.get(3));
        JobType J3 = new JobType("J3");
        ArrayList<Task> J3Tasks = new ArrayList<>();
        J3Tasks.add(testTasks.get(3)); //change to 1

        J1.setTasks(J1Tasks);
        J2.setTasks(J2Tasks);
        J3.setTasks(J3Tasks);

        testJobTypes.add(J1);
        testJobTypes.add(J2);
        testJobTypes.add(J3);

        testJobs = new ArrayList<>();
        testJobs.add(new tempJob("Job1",J1,0,30));
        testJobs.add(new tempJob("Job2",J1,2,29));
        testJobs.add(new tempJob("Job3",J2,5,40));
        testJobs.add(new tempJob("Job4",J2,7,35));
        testJobs.add(new tempJob("Job5",J3,10,30));

        testStations = new ArrayList<>();
        testStations.add(new Station("S1",1,false,false,1,20));
        testStations.add(new Station("S2",2,false,true,2,20));
        testStations.add(new Station("S3",2,false,true,1.5f,20));

        ArrayList<Task> testStation1Tasks = new ArrayList<>();
        testStation1Tasks.add(testTasks.get(0));
        testStation1Tasks.add(testTasks.get(1));
        testStations.get(0).setTasks(testStation1Tasks);
        ArrayList<Task> testStation2Tasks = new ArrayList<>();
        testStation2Tasks.add(testTasks.get(0));
        testStation2Tasks.add(testTasks.get(1));
        testStations.get(1).setTasks(testStation2Tasks);
        ArrayList<Task> testStation3Tasks = new ArrayList<>();
        testStation3Tasks.add(testTasks.get(2));
        testStation3Tasks.add(testTasks.get(3));
        testStations.get(2).setTasks(testStation3Tasks);
    }
    public void assignTestObjectsAsMain(){
        jobs = testJobs;
        tasks = testTasks;
        stations = testStations;
    }

    ArrayList<Task> sortTasksByEarliestDeadline(ArrayList<Task> inTasks){
        Collections.sort(inTasks, new TaskComparator());
        return inTasks;
    }

    Station getTheFreeStation(tempJob j){
        ArrayList<Station> usableStations = new ArrayList<>();
        for(Station s:stations){
            boolean canUseStation=false;
            for(Task task: j.getTasks()){
                if(s.getTasks().contains(task)){
                    canUseStation=true;
                    break;
                }
            }
            if(canUseStation){
                usableStations.add(s);
            }
        }
        return usableStations.get(0); //might add strategic selection later. That's why there is more than one usables added.
    }

    void EventHandler(Event event){
        if(event.getEventType()==Frontend.Workflow.EventType.ADD_TASK){
            
        }
        
    }
}

class tempJob{
    String name;
    JobType jobType; //to be replaced with JobType
    ArrayList<Task> tasks;
    double startTime;
    double duration;

    public tempJob(String name,JobType jobType,double startTime,double duration){
        this.name = name;
        this.jobType = jobType; 
        tasks = jobType.getTasks();
        this.startTime = startTime;
        this.duration = duration;
    }

    public void addTask(Task t){
        tasks.add(t);
    }

    public JobType getJobType() {
        return jobType;
    }

    public void setJobType(JobType jobType) {
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

    public String getName() {
        return name;
    }

}

class TaskComparator implements Comparator<Task> {
    public int compare(Task task1, Task task2) {
        if(task1.getValue()==task2.getValue()){
            return 0;
        }else if(task1.getValue()>task2.getValue()){
            return 1;
        }else{
            return -1;
        }
    }
}

class JobComparator implements Comparator<tempJob> {
    public int compare(tempJob job1, tempJob job2) {
        if(job1.getStartTime()==job2.getStartTime()){
            return 0;
        }else if(job1.getStartTime()<job2.getStartTime()){
            return -1;
        }else{
            return 1;
        }
    }
}

class Event{
    int eventStartTime;
    int eventEndTime;
    Workflow.EventType eventType;
    public Workflow.EventType getEventType() {
        return eventType;
    }
}