package Frontend;
import java.util.*;


import java.io.FileReader;
import java.io.IOException;

import Parser.*;
import Frontend.*;

/*NOTES
 * 1- I will assume that the Jobs go one by one and tasks arent mixed but rather done Job by Job.
 * 2- I will assume that the Job will select the station with the least job first.
 */


public class Workflow {
    static boolean testMode = false;
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

    public FrontendWorkflow(){
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
        
        for (JobType jobType : jobTypes) {
            for (Task task : jobType.getTasks()) {
                System.out.println(task.getName());
            }
        }

        System.out.println(tasks.get(0).getName());
        
        WorkflowManager();
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
    ArrayList<Task> sortTasksByEarliestDeadline(ArrayList<Task> inTasks){
        Collections.sort(inTasks, new TaskComparator());
        return inTasks;
    }
    ArrayList<Job> sortJobsByStartTime(ArrayList<Job> inJobs){
        Collections.sort(inJobs, new JobComparator());
        return inJobs;
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
        return usableStations.get(0); //might add strategic selection later. That's why there is more than one usables added.
    }
    Station getTheFreeStationByTask(Task t){
        ArrayList<Station> usableStations = new ArrayList<>();
        for(Station s:stations){
            boolean canUseStation=false;
            for(Task sTask : s.getDefaultTasks()){
                System.out.println(sTask.getName()+ " " + t.getName());
                if(sTask.getName().equals(t.getName())){
                    canUseStation=true;
                }
            }
        
            if(canUseStation){
                usableStations.add(s);
            }
        }
        return usableStations.get(0); //might add strategic selection later. That's why there is more than one usables added.
    }
  
    void WorkflowManager(){
        /* 
        System.out.println("In Workflow Manager.");
        for(tempJob job : jobs){
            System.out.println("For Job: " + job.getName());
            for(Task t : job.getTasks()){
                Station s = getTheFreeStationByTask(t);
                if(s.getMaxCapacity()>1){
                    ArrayList<Task> freeStationChannel = s.getFreeChannel();
                    freeStationChannel.add(getTaskFromStationByName(t, s));
                    System.out.println(t.getName() + " " + getTaskFromStationByName(t, s).getSpeed() + " " + s.getName() + " Multi Channel Station. Tasks in line: " + freeStationChannel.size() );
                }else{
                    s.getTaskChannels().get(0).add(t);
                    System.out.println("  " + t.getName() + " " + getTaskFromStationByName(t, s).getSpeed() + " "+ s.getName() + " Single Channel Station. Tasks in line: "+s.getTaskChannels().get(0).size());
                }
            }
        }
        */

        extractJobEventsFromJobList();
        HandleEvents();
    }

    void extractJobEventsFromJobList(){
        Collections.sort(jobs, new JobComparator());
        for(Job job : jobs){
            QueueJobEvent event = new QueueJobEvent(job,job.getStartTime());
            EventAdder(event);
        }
    }

    void extractTaskEventsFromJob(Job job){
        

        for(Task t : job.getTasks()){
            System.out.println("\nFor Job: " + job.getName());
            Station s = getTheFreeStationByTask(t);
            ArrayList<Task> freeStationChannel = s.getFreeChannel();
            Task currentTask = getTaskFromStationByName(t, s);
            System.out.println("got duration " + currentTask.getDuration());
            AddTaskEvent event = new AddTaskEvent(job.getStartTime(),currentTask,s,freeStationChannel);
            EventAdder(event);
            /* 
            if(freeStationChannel.size()==0){
                System.out.println("Remove Task Event new first task: " + currentTask.getName());
                RemoveTaskEvent removeTaskEvent = new RemoveTaskEvent(currentTask,s,freeStationChannel,0);
                EventAdder(removeTaskEvent);
            }
            */
        }

        
    }

    void EventAdder(EventTemplate event){
        eventTemplates.add(event);
    }
    
    void HandleEvents(){
        
        @SuppressWarnings("unchecked")
        ArrayList<EventTemplate> waitingEventList = (ArrayList)eventTemplates.clone();//concurrent modification fix.
        /* 
        for(EventTemplate event : waitingEventList){
            switch(event.getClass().getSimpleName()){
                case "AddTaskEvent":
                    AddTaskEvent addTaskEvent = (AddTaskEvent)event;
                    Task task = addTaskEvent.getTask();
                    addTaskEvent.getTargetChannel().add(task);
                    break;
                case "RemoveTaskEvent":
                    break;
                case "QueueJobEvent":
                    QueueJobEvent queueJobEvent = (QueueJobEvent)event;
                    //First things first the jobs should queue, then the tasks.
                    extractTaskEventsFromJob(queueJobEvent.getJob());
                    break;
                case "FinishJobEvent":
                    break;
            }
            

        }
        */
        
        int queueCount = 0;
        EventTemplate currentEvent;
        while(true){
            if(eventTemplates.get(queueCount).isDone()) queueCount++;
            currentEvent = eventTemplates.get(queueCount);
            switch(currentEvent.getClass().getSimpleName()){
                case "AddTaskEvent":
                    System.out.println("AddTaskEvent " + currentEvent.hashCode());
                    AddTaskEvent addTaskEvent = (AddTaskEvent)currentEvent;
                    Task task = addTaskEvent.getTask();
                    addTaskEvent.getTargetChannel().add(task);
                    System.out.println("Task: " + task.getName() + " is added to the queue of " + addTaskEvent.getTargetChannel().size() + " at channel " + addTaskEvent.getTargetStation().getTaskChannels().indexOf(addTaskEvent.getTargetChannel()));
                    addTaskEvent.setDone(true);

                    RemoveTaskEvent removeTaskEventOfTask = new RemoveTaskEvent(addTaskEvent.getTime() + task.getDuration(), task,addTaskEvent.getTargetStation(),addTaskEvent.getTargetChannel());
                    EventAdder(removeTaskEventOfTask);
                    break;
                case "RemoveTaskEvent":
                    
                    RemoveTaskEvent removeTaskEvent = (RemoveTaskEvent)currentEvent;
                    System.out.println("Remove task" + removeTaskEvent.getTask().getName());
                    removeTaskEvent.setDone(true);
                    Task currentTask = removeTaskEvent.getTask();
                    removeTaskEvent.getTargetChannel().remove(currentTask);

                    if(removeTaskEvent.getTargetChannel().size()==0)break;
                    Task nextTask = removeTaskEvent.getTargetChannel().getFirst();
                    AddTaskEvent nextAddTaskEvent = new AddTaskEvent(removeTaskEvent.getTime(), nextTask, removeTaskEvent.getTargetStation());
                    System.out.println("Next event time: " + nextAddTaskEvent.getTime());

                    /*
                    if(removeTaskEvent.getTargetChannel().size()!=0){
                        Task nextTask = removeTaskEvent.getTargetChannel().getFirst();
                        RemoveTaskEvent nextTaskEvent = new RemoveTaskEvent(nextTask, removeTaskEvent.getTargetStation(), removeTaskEvent.getTargetChannel(), removeTaskEvent.getTime());
                        EventAdder(nextTaskEvent);
                    }
                    */
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
            if(queueCount + 1 == eventTemplates.size()) break;
            queueCount++;
        }


        System.out.println();
        Collections.sort(eventTemplates, new EventComparator());
        for(EventTemplate e : eventTemplates){
            System.out.println(e.toString()  + " "+ e.hashCode());
        }

        for(Station s : stations){
            System.out.print("\n\nAt station " + s.getName() + " Channel queues ");
            for(ArrayList<Task> i : s.getTaskChannels()){
                System.out.print(i.size() + " ");
            }
            System.out.println();
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

class JobComparator implements Comparator<Job> {
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

