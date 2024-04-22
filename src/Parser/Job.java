
package Parser;
//made by Aykan Ugur all rights belong his fork

import java.util.ArrayList;

public class Job {
    
    ArrayList<Task> tasks = new ArrayList<>();
    String name;

    public Job(String name) {
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
    
    
    
    
    
}
