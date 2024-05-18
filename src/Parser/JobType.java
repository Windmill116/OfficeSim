
package Parser;
//made by Aykan Ugur all rights belong his fork

import java.util.ArrayList;

public class JobType {
    
    private ArrayList<Task> tasks;
    private String name;

    public JobType(String name) {
        this.name = name;
        this.tasks = new ArrayList<>();
    }

    public JobType(ArrayList<Task> tasks, String name) {
        this.tasks = tasks;
        this.name = name;
    }
    public ArrayList<Task> getTasks() {
        return tasks;
    }

    public void setTasks(ArrayList<Task> tasks) {
        this.tasks = tasks;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
    public String toString(){
        String s="";
        for (Task task : this.tasks) {
            s+=task.getName()+" ";
        }
        return this.name.toUpperCase()+":\nTasks of the job: "+s;
    }
    
    
    
}
