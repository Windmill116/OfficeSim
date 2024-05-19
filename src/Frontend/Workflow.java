package Frontend;
import java.util.*;


import java.io.FileReader;
import java.io.IOException;
import java.text.DecimalFormat;

import Parser.*;

/*
 * This is the main class for the whole program. 
 * It is responsible for managing the workflow and 
 * calling the appropriate functions to handle events 
 * and update the program's state.
 * 
 * 
 */

public class Workflow {
    public static void main(String[] args) {
        
        if(args.length < 2){
            System.out.println("USAGE: java office.jar <workflow file> <job file>");
            System.out.println("Arguments not found, will run on test mode.");
            startMenu("dataProvided.txt","job.txt");
            //return;
        }else{
            System.out.println("Arguments found, will run on normal mode.");
            startMenu(args[0],args[1]);
        }

        
    }

    public static void startMenu(String workflowName, String jobName){
        Scanner s1=new Scanner(System.in);

        System.out.println("Enter 'cancel' to end the program,\nEnter anything else to start the program in the normal mode.");
        String s=s1.nextLine();
        s1.close();
        if(s.toLowerCase().equals("cancel")){
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
                System.err.println("DEFINE OBJECTS LINE BY LINE DO NOT PUT OBJECTS IN SAME LINE");
                System.exit(0);
            }
        }
    }

    

    
}

class FrontendWorkflow{

    private ArrayList<Job> jobs;
    private ArrayList<JobType> jobTypes;
    private ArrayList<Station> stations;
    private ArrayList<Task> tasks;

    ArrayList<EventTemplate> eventTemplates = new ArrayList<EventTemplate>();

    Organizer organizer;
    /*
     * Constructor that initializes the workflow with the given Organizer.
     */
    public FrontendWorkflow(Organizer organizer){
        this.organizer = organizer;
        getArraysFromOrganizer(organizer);
        WorkflowManager();
    }
    /*
     * Extracts data from the organizer and starts the workflow manager.
     */
    public void getArraysFromOrganizer(Organizer organizer){

        jobs = organizer.getJobs();
        Collections.sort(jobs, new JobComparator());

        jobTypes = organizer.getJobTypes();
        
        stations = organizer.getStations();

        tasks = organizer.getTasks();
        
        
    }
    /*
     * Prints the workflow information including tasks, stations, and job types.
     */
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
            System.out.println();
            System.out.println(station.toString());
            for(Task t : station.getDefaultTasks()){
                System.out.print(t.toString() + " ");
            }
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

    /*
     * This function iterates over the list of stations 
     * to find those that can handle the given task. 
     * Among the eligible stations, it selects the one 
     * with the least number of tasks in its channel, 
     * indicating it is the least busy.
     */
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
    /*
     * This function extracts tasks from a job and 
     * schedules them in the appropriate stations, 
     * creating and scheduling AddTaskEvent and 
     * RemoveTaskEvent instances for each task.
     */
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
            float multiCheck = s.checkMultiFlag(currentTask, freeStationChannel);
            if(multiCheck>0||startTime<multiCheck) {
                startTime = multiCheck;
                System.out.println(">>MultiCheck reorganized " +currentTask.getName() +  "'s timetable.");
            } 
            AddTaskEvent addTaskEvent = new AddTaskEvent(startTime,currentTask,s,freeStationChannel);
            RemoveTaskEvent removeTaskEvent = new RemoveTaskEvent(startTime + currentTask.getDuration(), currentTask, s, freeStationChannel);
            System.out.println("For Task: " + currentTask.getName() + " added at station: " + s.getName()+ " at channel with" + addTaskEvent.getTargetChannel().toString() + " at time: " + startTime + " until: " + (startTime + currentTask.getDuration()));
            addTaskEvent.setJobOfTask(job);
            removeTaskEvent.setJobOfTask(job);
            s.getEvents().add(addTaskEvent);
            s.getEvents().add(removeTaskEvent);
            s.setDurationAddUpForUtilization(s.getDurationAddUpForUtilization() + currentTask.getDuration());
            job.getEventTemplates().add(removeTaskEvent);
            addTaskEvent.getTargetChannel().add(currentTask);
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
    /*
     * This function processes all events in the eventTemplates 
     * list in sequence. It handles different types of events 
     * (AddTaskEvent, RemoveTaskEvent, QueueJobEvent, FinishJobEvent) 
     * by executing the appropriate actions and updating the 
     * state of the workflow accordingly.
     */
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
                    System.out.println("Handle Events: \nAdd Task Event for: " + addTaskEvent.getTask().getName());
                    currentTask = addTaskEvent.getTask();
                    //addTaskEvent.getTargetChannel().add(currentTask);
                    addTaskEvent.getTargetStation().printWhatTasksAreExecuting();
                    break;
                case "RemoveTaskEvent":
                    RemoveTaskEvent removeTaskEvent = (RemoveTaskEvent) currentEvent;
                    System.out.println("Handle Events: \nRemove task event for: " + removeTaskEvent.getTask().getName());
                    currentTask = removeTaskEvent.getTask();
                    removeTaskEvent.getTargetStation().printWhatTasksAreExecuting();
                    removeTaskEvent.getTargetChannel().remove(currentTask);
                    System.out.println("Task removed from: " + removeTaskEvent.getTargetStation().getName());
                    removeTaskEvent.getTargetStation().printWhatTasksAreExecuting();
                    break;
                case "QueueJobEvent":
                    System.out.println("QueueJobEvent");
                    QueueJobEvent queueJobEvent = (QueueJobEvent)currentEvent;
                    //First things first the jobs should queue, then the tasks.
                    extractTaskEventsFromJob(queueJobEvent.getJob());
                    queueJobEvent.setDone(true);
                    break;
                case "FinishJobEvent":
                    FinishJobEvent finishJobEvent = (FinishJobEvent)currentEvent;
                    System.out.println("Finish Job Event for: " + finishJobEvent.getJob().getName() + " at " + dF.format(finishJobEvent.getTime())+ ".");
                    break; 
            }
            Collections.sort(eventTemplates, new EventComparator());
            System.out.println("\n");
            if(queueCount + 1 == eventTemplates.size()) break;
            queueCount++;
        }

        System.out.println(EventTemplate.ANSI_GREEN+"\n<=============="+EventTemplate.ANSI_RED+ " RECAP" +EventTemplate.ANSI_GREEN+" =============>\n" + EventTemplate.ANSI_RESET);
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
        if(job1.getDuration()==job2.getDuration()){
            return 0;
        }else if(job1.getDuration()<job2.getDuration()  && job1.getStartTime()==job2.getStartTime()){
            return -1;
        }else if(job1.getStartTime()==job2.getStartTime()){
            return 1;
        }else{
            return 0;
        }
    }
}

class EventComparator implements Comparator<EventTemplate>{
    @Override
    public int compare(EventTemplate o1, EventTemplate o2) {
        return o1.compareTo(o2);
    }
}

