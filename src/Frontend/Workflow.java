package Frontend;
import java.util.*;


import java.io.FileReader;
import java.io.IOException;
import java.text.DecimalFormat;

import Parser.*;

/*NOTES
 * 1- I will assume that the Jobs go one by one and tasks arent mixed but rather done Job by Job.
 * 2- I will assume that the Job will select the station with the least job first.
 */


public class Workflow {
    static boolean testMode = false;
    public static void main(String[] args) {
        if(args.length < 2){
            System.out.println("USAGE: java office.jar <workflow file> <job file>");
            return;
        }

        startMenu(args[0],args[1]);
    }

    public static void startMenu(String workflowName, String jobName){
        Scanner s1=new Scanner(System.in);

        System.out.println("Enter 'test' to start the program in test mode,\nEnter 'cancel' to end the program,\nEnter anything else to start the program in the normal mode.");
        String s=s1.nextLine();
        s1.close();

        if(s.toLowerCase().equals("test")){
            FrontendWorkflow testFrontendWorkflow;
            testFrontendWorkflow = new FrontendWorkflow();
        } else if(s.toLowerCase().equals("cancel")){
            return;
        } else{
            FrontendWorkflow testFrontendWorkflow;
            Organizer organizer;
            try {
                
                FileReader jb = new FileReader(jobName);
                FileReader fr = new FileReader(workflowName);
                Parser p = new Parser(fr,jb);// add job file to fix problem
                p.start();
                ArrayList<String> tokens = p.getTokens();
                organizer = new Organizer(tokens,p.getLine(),p.getJobTokens());

                testFrontendWorkflow = new FrontendWorkflow(organizer);
                
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    

    
}

class FrontendWorkflow{

    private ArrayList<Job> jobs;
    private ArrayList<JobType> jobTypes;
    private ArrayList<Station> stations;
    private ArrayList<Task> tasks;

    ArrayList<tempJob> testJobs;
    ArrayList<Task> testTasks;
    ArrayList<JobType> testJobTypes;
    ArrayList<Station> testStations;

    ArrayList<Object> eventList = new ArrayList<>();

    ArrayList<EventTemplate> eventTemplates = new ArrayList<EventTemplate>();

    Organizer organizer;
    public FrontendWorkflow(Organizer organizer){
        this.organizer = organizer;
        getArraysFromOrganizer(organizer);
    }

    public FrontendWorkflow(){      //This is for test mode
        
        createTestObjects();
        assignTestObjectsAsMain();

        WorkflowManager();
    }
    public void getArraysFromOrganizer(Organizer organizer){
        //jobs = organizer.getJobs();

        jobs = organizer.getJobs();

        jobTypes = organizer.getJobTypes();
        
        stations = organizer.getStations();

        tasks = organizer.getTasks();

        
        /*
        for (JobType jobType : jobTypes) {
            for (Task task : jobType.getTasks()) {
                System.out.println(task.getName());
            }
        }

        System.out.println(tasks.get(0).getName());
        */
        
        WorkflowManager();
    }

    private void printWorkflowInfo(){
        System.out.println("---WORKFLOW INFO---");
        System.out.println();
        System.out.println("Tasks:");
        System.out.println();
        for (Task task : tasks) {
            System.out.println(task.toString());
            System.out.println();
        }
        System.out.println();
        System.out.println("Stations:");
        System.out.println();
        for (Station station : stations) {
            System.out.println(station.toString());
            System.out.println();
        }
        System.out.println();
        System.out.println("Job types:");
        System.out.println();
        for (Job job : jobs) {
            System.out.println(job.toString());
            System.out.println();
        }
    }

    public void createTestObjects(){
        /*Job1 J1 1 30
        Job2 J1 2 29
        Job3 J2 5 40
        Job4 J2 7 35
        Job5 J3 10 30 */

        testTasks= new ArrayList<>();
        testTasks.add(new Task("T1",10));
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
        testStations.add(new Station("S2",2,true,true,2,20));
        testStations.add(new Station("S3",2,true,true,1.5f,20));

        ArrayList<Task> testStation1Tasks = new ArrayList<>();
        testStation1Tasks.add(testTasks.get(0));
        testStation1Tasks.add(testTasks.get(1));
        testStations.get(0).setDefaultTasks(testStation1Tasks);
        ArrayList<Task> testStation2Tasks = new ArrayList<>();
        testStation2Tasks.add(testTasks.get(0));
        testStation2Tasks.add(testTasks.get(1));
        testStations.get(1).setDefaultTasks(testStation2Tasks);
        ArrayList<Task> testStation3Tasks = new ArrayList<>();
        testStation3Tasks.add(testTasks.get(2));
        testStation3Tasks.add(testTasks.get(3));
        testStations.get(2).setDefaultTasks(testStation3Tasks);
    }
    public void assignTestObjectsAsMain(){
        //jobs = testJobs;
        tasks = testTasks;
        stations = testStations;
    }
    Station getTheFreeStationByJob(tempJob j){
        ArrayList<Station> usableStations = new ArrayList<>();
        for(Station s:stations){
            
            boolean canUseStation=false;
            for(Task task: j.getTasks()){
                if(s.getDefaultTasks().contains(task)){
                    canUseStation=true;
                    break;
                }
            }
            if(canUseStation){
                usableStations.add(s);
            }
        }
        return usableStations.get(0);
    }
    Station getTheFreeStationByTask(Task t){
        ArrayList<Station> usableStations = new ArrayList<>();
        for(Station s:stations){
            
            boolean canUseStation=false;
            for(Task sTask : s.getDefaultTasks()){
                if(sTask.getName().equals(t.getName())){
                    canUseStation=true;
                }
            }
        
            if(canUseStation){
                usableStations.add(s);
            }
        }
        if(usableStations.size()==0) return null;

        int leastCount = 9999;
        Station leastBusyStation = new Station(null, leastCount, false, false, leastCount, leastCount);
        for(Station s: usableStations){
            if(leastCount>s.getFreeChannel().size()) {
                leastCount = s.getFreeChannel().size();
                leastBusyStation = s;
            }
        }
        return leastBusyStation;
    }
  
    void WorkflowManager(){
        printWorkflowInfo();
        extractJobEventsFromJobList();
        HandleEvents();
    }

    void extractJobEventsFromJobList(){
        Collections.sort(jobs, new JobComparator());        //Sort jobs according to their start date
        for(Job job : jobs){
            QueueJobEvent event = new QueueJobEvent(job,job.getStartTime());
            EventAdder(event);
        }
    }

    ArrayList<AddTaskEvent> extractTaskEventsFromJob(Job job){      //Extract tasks from jobs and put it in line in relevant stations, handleEvents calls it
        
        ArrayList<AddTaskEvent> jobsAddTaskEvents = new ArrayList<>();
        System.out.println("\nFor Job: " + job.getName());
         
        for(Task t : job.getTasks()){
            
            Station s = getTheFreeStationByTask(t);
            if(s==null)continue;
            ArrayList<Task> freeStationChannel = s.getFreeChannel();
            Task currentTask = getTaskFromStationByName(t, s);

            float startTime=0;
            if(s.getEvents().size()==0){
                startTime = job.getStartTime();
            }else{
                if(job.getStartTime()>s.getEvents().getLast().getTime()){
                    startTime = job.getStartTime();
                }else startTime = s.getEvents().getLast().getTime();
            }

            AddTaskEvent addTaskEvent = new AddTaskEvent(startTime,currentTask,s,freeStationChannel);
            RemoveTaskEvent removeTaskEvent = new RemoveTaskEvent(startTime + currentTask.getDuration(), currentTask, s, freeStationChannel);
            System.out.println("For Task: " + currentTask.getName() + " added at station: " + s.getName() + " at time: " + startTime + " until: " + (startTime + currentTask.getDuration()));
            addTaskEvent.setJobOfTask(job);
            removeTaskEvent.setJobOfTask(job);
            s.getEvents().add(addTaskEvent);
            s.getEvents().add(removeTaskEvent);
            s.setDurationAddUpForUtilization(s.getDurationAddUpForUtilization() + currentTask.getDuration());
            job.getEventTemplates().add(removeTaskEvent);

            EventAdder(addTaskEvent);
            EventAdder(removeTaskEvent);
        }
        job.calculateJobTardiness();
        FinishJobEvent finishJobEvent = new FinishJobEvent(job, job.getSimulativeFinishTime());
        EventAdder(finishJobEvent);
        return jobsAddTaskEvents;
    }

    void EventAdder(EventTemplate event){
        eventTemplates.add(event);
    }
    
    void HandleEvents(){
        DecimalFormat dF = new DecimalFormat("#.##");
        int queueCount = 0;
        EventTemplate currentEvent;
        Task currentTask;
        while(true){
            if(eventTemplates.get(queueCount).isDone()) queueCount++;
            currentEvent = eventTemplates.get(queueCount);
            switch(currentEvent.getClass().getSimpleName()){
                case "AddTaskEvent":
                    AddTaskEvent addTaskEvent = (AddTaskEvent)currentEvent;
                    System.out.println("Add Task Event for: " + addTaskEvent.getTask().getName());
                    currentTask = addTaskEvent.getTask();
                    addTaskEvent.getTargetChannel().add(currentTask);
                    addTaskEvent.getTargetStation().printWhatTasksAreExecuting();
                    System.out.println("Task added to the " + addTaskEvent.getTargetStation().getName());
                    break;
                case "RemoveTaskEvent":

                    RemoveTaskEvent removeTaskEvent = (RemoveTaskEvent) currentEvent;
                    System.out.println("Remove task event for: " + removeTaskEvent.getTask().getName());
                    currentTask = removeTaskEvent.getTask();
                    removeTaskEvent.getTargetChannel().remove(currentTask);
                    System.out.println("Task removed from: " + removeTaskEvent.getTargetStation().getName());
                    break;
                case "QueueJobEvent":
                    System.out.println("QueueJobEvent");
                    QueueJobEvent queueJobEvent = (QueueJobEvent)currentEvent;
                    //First things first the jobs should queue, then the tasks.
                    extractTaskEventsFromJob(queueJobEvent.getJob());
                    queueJobEvent.setDone(true);
                    break;
                case "FinishJobEvent":
                    break; 
            }
            Collections.sort(eventTemplates, new EventComparator());
            System.out.println("\n");
            if(queueCount + 1 == eventTemplates.size()) break;
            queueCount++;
        }

        System.out.println();
        Collections.sort(eventTemplates, new EventComparator());
        for(EventTemplate e : eventTemplates){
            System.out.println(e.toString());
        }
        System.out.println();
        for(Station s : stations){
            float lastEventTime = eventTemplates.getLast().getTime();
            System.out.println("Utilization for " + s.getName() + " is %" +dF.format((s.getDurationAddUpForUtilization()/lastEventTime)*100));
        }
        System.out.println();
        for(Job job : jobs){
            if(job.getJobTardiness()>0){
                System.out.println(job.getName() + " tardiness is " + dF.format(job.getJobTardiness()) + " minutes.");
            }else if(job.getJobTardiness()<0){
                System.out.println(job.getName() + " finished " + dF.format(-job.getJobTardiness())+" minutes early.");
            }else{
                System.out.println("Job finished just in time.");
            }
            
        }
    }

    //on getTaskFromStationByName we get the task from the station by the name of the given task parameter,
    //we make a new task and we set it to station's 
    //then we set the value of the task to job files value.

    Task getTaskFromStationByName(Task t, Station s){
        ArrayList<Task> tasks = s.getDefaultTasks();
        for(Task sTask : tasks){
            if(sTask.getName().equals(t.getName())){
                Task t1 = sTask;
                t1.setValue(t.getValue());
                t1.calculateDuration();
                return t1;
            }
        }
        return null;
    }

}

class tempJob{
    String name;
    JobType jobType;
    ArrayList<Task> tasks;
    int startTime;
    int duration;

    public tempJob(String name,JobType jobType,int startTime,int duration){
        this.name = name;
        this.jobType = jobType; 
        tasks = jobType.getTasks();
        this.startTime = startTime;
        this.duration = duration;
    }

    public Task getTask(int index){
        return this.tasks.get(index);
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

    public int getStartTime() {
        return startTime;
    }

    public void setStartTime(int startTime) {
        this.startTime = startTime;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public String getName() {
        return name;
    }

}

class TaskComparator implements Comparator<Task> {          //Compare according to their
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
class JobComparator implements Comparator<Job> {        //Sort jobs according to start time
    public int compare(Job job1, Job job2) {
        if(job1.getStartTime()==job2.getStartTime()){
            return 0;
        }else if(job1.getStartTime()<job2.getStartTime()){
            return -1;
        }else{
            return 1;
        }
    }
}

class EventComparator implements Comparator<EventTemplate>{
    @Override
    public int compare(EventTemplate o1, EventTemplate o2) {
        return o1.compareTo(o2);
    }
}

