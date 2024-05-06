package Frontend;
import Parser.*;

class AddTaskEvent{
    int time;
    Task task;
    Station targetStation;
    int targetChannel;

    AddTaskEvent(int time,Task task, Station targetStation, int targetChannel){
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
