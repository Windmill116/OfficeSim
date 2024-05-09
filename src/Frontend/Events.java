package Frontend;
import java.util.ArrayList;

import Parser.*;

/*
 * Every event should have time as it's first property.
 */

class AddTaskEvent{
    int time;
    Task task;
    Station targetStation;
    ArrayList<Task> targetChannel;

    AddTaskEvent(int time,Task task, Station targetStation, ArrayList<Task> targetChannel){
        this.time = time;
        this.task = task;
        this.targetStation = targetStation;
        this.targetChannel = targetChannel;
    }

    AddTaskEvent(int time,Task task, Station targetStation){
        this.time = time;
        this.task = task;
        this.targetStation = targetStation;
    }

    AddTaskEvent(){
        
    }
}
class RemoveTaskEvent{
    int time;
    Task task;
    
    RemoveTaskEvent(int time,Task task){
        this.task = task;
    }
}

class QueueJobEvent{
    tempJob job;
    int time;

    public tempJob getJob() {
        return job;
    }

    public void setJob(tempJob job) {
        this.job = job;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    QueueJobEvent(int time,tempJob job){
        this.job = job;
        this.time = time;
    }
}

class FinishJobEvent{
    Job job;
    int time;

    FinishJobEvent(Job job, int time){
        this.job = job;
        this.time = time;
    }
}
