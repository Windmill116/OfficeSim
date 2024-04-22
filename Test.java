import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import Parser.Parser;
public class Test {
    public static void main(String[] args) {
        try { //It's apperant that the code was NOT written by the Communist of the group,
                //Because it separates the lines.
            FileReader fr = new FileReader("test.txt");
            Parser p = new Parser(fr);
            p.workflowTokenizer();
            ArrayList<String> tokens = p.getTokens();
            for(String s: tokens){
                System.out.println(s);
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }



    }
}

class Station{
    private String name;
    private boolean multiFlag;
    private boolean fifoFlag;

    Station(String name, boolean multiFlag, boolean fifoFlag){
        this.name = name;
        this.multiFlag = multiFlag;
        this.fifoFlag = fifoFlag;
    }
}

class Job{
    private String name;
    private JobType type;
    private float startTime;
    private float duration;
    
    Job(String name, JobType type, float startTime, float duration){
        this.name = name;
        this.type = type;
        this.startTime = startTime;
        this.duration = duration;
    }
}

class JobType{
    private ArrayList<Task> tasks;
    private String name;

    JobType(ArrayList<Task> tasks, String name){
        this.tasks = tasks;
        this.name = name;
    }
}

class Task{
    private String name;
    private float size;

    Task(String name, float size){
        this.name = name;
        this.size = size;
    }
}