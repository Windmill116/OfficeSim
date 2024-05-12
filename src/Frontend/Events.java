package Frontend;
import Parser.*;
import java.util.*;


abstract class EventTemplate implements Comparable<EventTemplate>{
    int time;

    @Override
    public int compareTo(EventTemplate o) {
        return Integer.compare(this.time, o.time);
    }
}

class AddTaskEvent extends EventTemplate{
    Task task;
    Station targetStation;
    ArrayList<Task> targetChannel;

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public Task getTask() {
        return task;
    }

    public void setTask(Task task) {
        this.task = task;
    }

    public Station getTargetStation() {
        return targetStation;
    }

    public void setTargetStation(Station targetStation) {
        this.targetStation = targetStation;
    }

    public ArrayList<Task> getTargetChannel() {
        return targetChannel;
    }

    public void setTargetChannel(ArrayList<Task> targetChannel) {
        this.targetChannel = targetChannel;
    }

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

    @Override
    public String toString() {
        return ("Name: " + task.getName() + " Time: " + time + " Station: " + targetStation.getName());
    }
}
class RemoveTaskEvent{
    Task task;
    
    RemoveTaskEvent(Task task){
        this.task = task;
    }
}

class QueueJobEvent extends EventTemplate{
    tempJob job;

    QueueJobEvent(tempJob job, int time){
        this.job = job;
        this.time = time;
    }

    public tempJob getJob() {
        return job;
    }

    @Override
    public String toString() {
        return ("Job: " + job.getName() + " Time: " + time);
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
