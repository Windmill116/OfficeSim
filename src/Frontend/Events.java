package Frontend;
import Parser.*;
import java.util.*;


abstract class EventTemplate implements Comparable<EventTemplate>{
    float time;
    boolean done = false;
    public boolean isDone() {
        return done;
    }
    public void setDone(boolean done) {
        this.done = done;
    }
    @Override
    public int compareTo(EventTemplate o) {
        return Float.compare(this.time, o.time);
    }
}

class AddTaskEvent extends EventTemplate{
    private Task task;
    private Station targetStation;
    private ArrayList<Task> targetChannel;

    public float getTime() {
        return time;
    }

    public void setTime(float time) {
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

    AddTaskEvent(float time,Task task, Station targetStation, ArrayList<Task> targetChannel){
        this.time = time;
        this.task = task;
        this.targetStation = targetStation;
        this.targetChannel = targetChannel;


    }

    AddTaskEvent(float time,Task task, Station targetStation){
        this.time = time;
        this.task = task;
        this.targetStation = targetStation;
    }

    AddTaskEvent(){
        
    }

    @Override
    public String toString() {
        //return ("Name: " + task.getName() + " Time: " + time + " Station: " + targetStation.getName());
        return ("Added task is " + task.getName() + " on " + time + " at the station " + targetStation.getName() + " in a channel with " + targetChannel.size() + " tasks in queue.");
    }
}
class RemoveTaskEvent extends EventTemplate{
    private Task task;
    private Station targetStation;
    private ArrayList<Task> targetChannel;

    public float getTime() {
        return time;
    }

    public void setTime(float time) {
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

    RemoveTaskEvent(Task task, Station targetStation, ArrayList<Task> targetChannel, float previousTime) {
        if(targetStation.getPlusMinus()!=0){
            this.time = (Float) ((task.getPlusMinus()+1)*(task.getValue()/task.getSpeed()));
        }else{
            this.time = (Float) (task.getValue()/task.getSpeed());
            
        }

        this.time += previousTime;
        this.task = task;
        this.targetStation = targetStation;
        this.targetChannel = targetChannel;


    }

    RemoveTaskEvent(float time,Task task, Station targetStation){
        this.time = time;
        this.task = task;
        this.targetStation = targetStation;
    }

    RemoveTaskEvent(){
        
    }

    @Override
    public String toString() {
        //return ("Name: " + task.getName() + " Time: " + time + " Station: " + targetStation.getName());
        return ("Removed task is " + task.getName() + " with a value of " + task.getValue() + " in speed of " + task.getSpeed() + " on " + time + " at the station " + targetStation.getName() + " in a channel with " + targetChannel.size() + " tasks in queue.");
    }
}

class ExecuteTaskEvent extends EventTemplate{
    private Task task;
    private Station targetStation;
    private ArrayList<Task> targetChannel;

    public float getTime() {
        return time;
    }

    public void setTime(float time) {
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

    ExecuteTaskEvent(Task task, Station targetStation, ArrayList<Task> targetChannel){
        if(targetStation.getPlusMinus()!=0){
            this.time = (Float) (task.getPlusMinus()+1)*(task.getValue()/task.getSpeed());
            System.out.println(time);
        }else{
            this.time = (Float) (task.getValue()/task.getSpeed());
            System.out.println(time);
        }

        this.task = task;
        this.targetStation = targetStation;
        this.targetChannel = targetChannel;


    }

    ExecuteTaskEvent(float time,Task task, Station targetStation){
        this.time = time;
        this.task = task;
        this.targetStation = targetStation;
    }

    ExecuteTaskEvent(){
        
    }

    @Override
    public String toString() {
        //return ("Name: " + task.getName() + " Time: " + time + " Station: " + targetStation.getName());
        return ("Removed task is " + task.getName() + " with a value of " + task.getValue() + " in speed of " + task.getSpeed() + " on " + time + " at the station " + targetStation.getName() + " in a channel with " + targetChannel.size() + " tasks in queue.");
    }
}

class QueueJobEvent extends EventTemplate{
    tempJob job;

    QueueJobEvent(tempJob job, float time){
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
    float time;

    FinishJobEvent(Job job, float time){
        this.job = job;
        this.time = time;
    }
}
