package Frontend;
import Parser.*;

class AddTaskEvent{
    int time;
    Task task;
    Station targetStation;
    int targetChannel;

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
}
class RemoveTaskEvent{
    Task task;
    
    RemoveTaskEvent(Task task){
        this.task = task;
    }
}

class QueueJobEvent{
    Job job;
    int time;

    QueueJobEvent(Job job, int time){
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
