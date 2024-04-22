
package Parser;
//made by Aykan Ugur all rights belong his coat

import java.util.ArrayList;


public class Organizer {
    private boolean typeFinder,typeRead,stationFinder;
   
    private ArrayList<String> tokens; // in
    private ArrayList<Task> tasks = new ArrayList<>();
    private ArrayList<Job> jobs = new ArrayList<>();
    private ArrayList<Station> stations = new ArrayList<>();
    private int index = 0;
    public Organizer(ArrayList<String> tokens) {
        this.tokens = tokens;
        organizeTask(); // run organizer
    }
    
    private void organizeTask()
    {
        String ttt = null;
        for (String token : tokens) {
            index++;
           
            if(typeFinder)
            {
                
                typeFinder = false;
                ttt= token;
                
            }else
            {
               if(token.equals("("))
               {
                  typeFinder = true; //start type finder
                  continue;
               }
               if(token.equals(")"))
               {
                  organizeJobs();
                  break; 
               } 
                try {
                            
                     float val = (float)Double.parseDouble(token);
                     tasks.get(tasks.size()-1).setValue(val);
                        
                    } catch (NumberFormatException e) {
                        
                        tasks.add(new Task(token,0));
                    }
            }
        }
    }
    
    private void organizeJobs()
    {
        boolean jobFinder = false;
        index += 2;
        for (int i =index ; i < tokens.size(); i++) {
           
          if(tokens.get(i).equals("("))
          {
              //after this its time for jobs
              jobFinder = true;
              jobs.add(new Job(tokens.get(i+1)));
              i++;
              continue;
          }
          if(jobFinder)
          {
              if(tokens.get(i).equals(")"))
              {
                  
                   if(tokens.get(i+4).equals("stations")) 
                   {
                       i = i + 4;
                       index = i;
                       organizeStations();
                       break;
                   }
                   jobFinder = false;
                   continue;
              }     
                   try {
                            
                    float val = (float)Double.parseDouble(tokens.get(i));
                    ArrayList<Task> ts = jobs.get(jobs.size()-1).getTasks();
                    ts.get(ts.size()-1).setValue(val);
                       
                        
                    } catch (NumberFormatException e) {
                        
                       for (Task task : tasks) {
                  
                   if(tokens.get(i).equals(task.getName()))
                   {
                       jobs.get(jobs.size()-1).getTasks().add(task);
                   }
                    }
              }
          }
        }
    }
    
    private void organizeStations()
    {
        index++;
        for (int i = index; i < tokens.size(); i++) {
            
            if(tokens.get(i).equals("(")&& stationFinder == false)
            {
                String name = tokens.get(i+1);
                float maxCapacity = (float)Double.parseDouble(tokens.get(i+2));
                boolean mutliFlag = false;
                boolean fifoflag = false;
                if(tokens.get(i+3).equals("Y")) mutliFlag = true;
                if(tokens.get(i+4).equals("Y")) fifoflag = true;
                stations.add(new Station(name,maxCapacity,mutliFlag,fifoflag,0,0));
                i = i + 4;
                stationFinder = true;
                continue;
            }
            if(stationFinder)
            {
                if(tokens.get(i).equals(")"))
                {
                    //to do 
                    try {
                        tokens.get(i+4);
                    } catch (Exception e) {
                        
                        
                        break;
                    }
                    stationFinder = false;
                }else
                {
                    try {
                            
                       float val = (float)Double.parseDouble(tokens.get(i));
                       ArrayList<Task> ts = stations.get(stations.size()-1).getTasks();
                       ts.get(ts.size()-1).setValue(val);
                       
                       try {
                           
                           float val2 = (float)Double.parseDouble(tokens.get(i+1));
                          
                           stations.get(stations.size()-1).setPlusMinus(val2);
                           
                       } catch (NumberFormatException e) {
                           
                       }
                        
                    } catch (NumberFormatException e) {
                        
                       for (Task task : tasks) {
                  
                   if(tokens.get(i).equals(task.getName()))
                   {
                       stations.get(stations.size()-1).getTasks().add(task);
                   }
                    }
              }
                }
                
            }
        }
    }

    public ArrayList<Task> getTasks() {
        return tasks;
    }

    public void setTasks(ArrayList<Task> tasks) {
        this.tasks = tasks;
    }

    public ArrayList<Job> getJobs() {
        return jobs;
    }

    public void setJobs(ArrayList<Job> jobs) {
        this.jobs = jobs;
    }

    public ArrayList<Station> getStations() {
        return stations;
    }

    public void setStations(ArrayList<Station> stations) {
        this.stations = stations;
    }
    
    
    
}
