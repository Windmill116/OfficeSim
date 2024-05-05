
package Parser;
//made by Aykan Ugur all rights belong his coat
import java.util.ArrayList;
public class Organizer {
    private boolean typeFinder,typeRead,stationFinder;
   
    private ArrayList<String> tokens; // in
    private ArrayList<Task> tasks = new ArrayList<>();
    private ArrayList<JobType> jobs = new ArrayList<>();
    private ArrayList<Station> stations = new ArrayList<>();
    private ArrayList<String> jobTokens; // in 
    private ArrayList<Job> jobArrayList = new ArrayList<>();
    private int index = 0;
    private int index2 = 0;
    private boolean error;
    private int line = 1;
    private boolean newLine = false;
    private boolean firstTime = false;
    private int maxLine;
    private String text2;
    private Boolean jobtypesB = false;
    private boolean stationTypesB = false;
    public Organizer(ArrayList<String> tokens,int maxLine,ArrayList<String> jobTokens) { //Constructor
        this.tokens = tokens;
        this.maxLine = maxLine;
        this.jobTokens = jobTokens;
        for (String token : tokens) { //checking fatal errors before start the start
            if(token.equals("jobtypes"))
            {
                jobtypesB = true;
            }
            if(token.equals("stations"))
            {
                stationTypesB = true;
               
            }
            if(jobtypesB&&stationTypesB) break;
            
        }
      if(jobtypesB&&stationTypesB) // if everything is ok start to check tasks
      {
          taskErrorDetector();
      }else // if its not ok print whats wrong
      {
          if(!jobtypesB)
          {
              System.err.println("jobtypes is not exist and it will cause a lots of problem before start the program fix it");
          }else
          {
               System.err.println("stations is not exist and it will cause a lots of problem before start the program fix it");
          }
      }
    }
    
    private void organizeTask() // after task error check this code create task objects and send values to the objects
    {
        for (String token : tokens) { 
            
            if(token.equals(":line:"))continue; // ignore the :line: its not in data its created by computer
            index++;
            
            if(typeFinder) // start type finder
            {
                typeFinder = false;
                
            }else
            {
               if(token.equals("("))
               {
                  typeFinder = true; //start type finder
                  continue;
               }
               if(token.equals(")")) // stop the organize task and start to check jobs
               {
                  jobErrorDetector();
                  break; 
               } 
                try {
                            
                     float val = (float)Double.parseDouble(token); // if token is digit its add this value prev. task object
                     tasks.get(tasks.size()-1).setValue(val);
                        
                    } catch (NumberFormatException e) {
                        
                        tasks.add(new Task(token)); // create new task object if token is not digit
                    }
            }
        }
    }
    
    private void organizeJobTypes() // after jobs error check this code create job objects and send  values to the objects
    {
        boolean jobFinder = false; // define job finder
        index += 3; // start with jobtypes
        for (int i =index ; i < tokens.size(); i++) {
            if(tokens.get(i).equals(":line:"))continue; // ignore the :line: its not in data its created by computer
               
           
          if(tokens.get(i).equals("(")) // start to define jobs
          {
              jobFinder = true; // set jobfinder as true
              jobs.add(new JobType(tokens.get(i+1))); // create new job object
              i++; // next --> job values
              continue;
          }
          if(jobFinder)
          {
              if(tokens.get(i).equals(")")) // until ) add values to job
              {
                  
                   if(tokens.get(i+4).equals("stations")) // this code is a little bit problem in first problem pls check this code!!
                   {
                       i = i + 4; // to skip useless tokens
                       index = i; // set index as i 
                       StationErrorDetector(); // after finished to organize jobs start to check stations
                       break;
                   }
                   jobFinder = false; // stop job finder
                   continue;
              }     
                   try {
                            
                    float val = (float)Double.parseDouble(tokens.get(i)); // check is token a value ?
                    ArrayList<Task> ts = jobs.get(jobs.size()-1).getTasks(); //if it is digit send values to task
                    ts.get(ts.size()-1).setValue(val);
                       
                    } catch (NumberFormatException e) {
                        
                       for (Task task : tasks) {
                  
                   if(tokens.get(i).equals(task.getName()))
                   {
                       jobs.get(jobs.size()-1).getTasks().add(new Task(task.getName(),task.getValue())); // send task object to job object (has to)
                   }
                    }
              }
          }
        }
    }
    
    private void organizeStations() // after stations error check , this code create station objects and send values to the that objects
    {
        index++; // to skip useless tokens
        for (int i = index; i < tokens.size(); i++) {
           if(tokens.get(i).equals(":line:"))continue; // ignore the :line: its not in data its created by computer
            if(tokens.get(i).equals("(")&& stationFinder == false)  //if station finder is not working and its start of stations values
            {
                String name = tokens.get(i+1);
                float maxCapacity = (float)Double.parseDouble(tokens.get(i+2)); // check max capacity
                boolean mutliFlag = false;
                boolean fifoflag = false;
                if(tokens.get(i+3).equals("Y")) mutliFlag = true; // check multiflag
                if(tokens.get(i+4).equals("Y")) fifoflag = true; // check fifoflag
                stations.add(new Station(name,maxCapacity,mutliFlag,fifoflag,0,0)); // create a station object and send it to arraylist
                i = i + 4; // to skip useless tokens
                stationFinder = true; // start stationFinder
                continue;
            }
            if(stationFinder)
            {
                if(tokens.get(i).equals(")"))
                {
                    
                    try {
                        tokens.get(i+4);
                    } catch (Exception e) {
                       jobOrganizer();
                        break;
                    }
                    stationFinder = false;
                }else
                {
                    try {
                       float val = (float)Double.parseDouble(tokens.get(i)); //to check is token a digit
                       ArrayList<Task> ts = stations.get(stations.size()-1).getTasks(); // if it is a digit start this code block.
                       ts.get(ts.size()-1).setValue(val);
                       
                       try {
                           
                           float val2 = (float)Double.parseDouble(tokens.get(i+1)); //to check is token a digit
                          
                           stations.get(stations.size()-1).setPlusMinus(val2);
                           
                       } catch (NumberFormatException e) {
                           
                       }
                        
                    } catch (NumberFormatException e) {
                        
                       for (Task task : tasks) {
                  
                   if(tokens.get(i).equals(task.getName())) // if its not digit send task object to stations object
                   {
                       stations.get(stations.size()-1).getTasks().add(new Task(task.getName(),task.getValue()));
                   }
                    }
              }
                }
                
            }
        }
    }
//*** START OF GET AND SET OBJECTS***
    public ArrayList<Task> getTasks() {
        return tasks;
    }

    public void setTasks(ArrayList<Task> tasks) {
        this.tasks = tasks;
    }

    public ArrayList<JobType> getJobs() {
        return jobs;
    }

    public void setJobs(ArrayList<JobType> jobs) {
        this.jobs = jobs;
    }

    public ArrayList<Station> getStations() {
        return stations;
    }

    public void setStations(ArrayList<Station> stations) {
        this.stations = stations;
    }
    //*** END OF GET AND SET OBJECTS***
    private void taskErrorDetector() // this method check task errors
    {
        
        ArrayList<String> tasks2 = new ArrayList<String>(); //to check same tasks
       if(!tokens.get(1).equals("("))// check for (
       {
           System.out.println("line: "+line + "( is missing");
           error = true;
       }
       if(!tokens.get(2).equals("tasktypes")) // check for tasktypes is wrong or not
       {
           error = true;
           System.out.println("line: " + line + " you wrote tasktypes wrong");
       }
        for (int i = 3; i < tokens.size(); i++) {
           
                if(tokens.get(i).equals(":line:")) // ignore :line: tokens
                {
                  line++;
                  continue;
                }
            
            if(tokens.get(i).equals("jobtypes")) // to end the code
            {
                if(!tokens.get(i-1).equals("("))
                {
                    System.out.println("line: "+line + "( is missing"); 
                    error = true;
                }
                if(!tokens.get(i-3).equals(")"))
                {
                   System.out.println("line: " +(line-1)+ ") is missing"); 
                   error = true;
                }
                index2 = i+1 ; // to skip useless tokens
                break;
            }
                try {
                            
                     float val = (float)Double.parseDouble(tokens.get(i)); // check is it digit 
                     if(val<0)
                     {
                         System.out.println("line: "+line+ " " + tokens.get(i) + " value of task is smaller than 0 ");
                         error = true;
                         continue;
                     }
                     
                        
                    } catch (NumberFormatException e) {
                     
                       if(tasks2.contains(tokens.get(i))) // if it is not digit start this code
                       {
                           error = true;
                           System.out.println("line: "+line+ " " + tokens.get(i) + " is already defined");
                           continue;
                       }else
                       {
                           tasks2.add(tokens.get(i));
                           continue;
 
                       }
                        
                    }
                
               
        }
        
        error(error,"task"); // start error method
        if(!error)
        {
           organizeTask(); // if there is no error start organizetask
        }
    }
    
    private void jobErrorDetector()
    {
         ArrayList<String> jobs2 = new ArrayList<String>();
        for (int i = index2; i < tokens.size(); i++) {
          
                if(!newLine)
                {  
                if(tokens.get(i).equals(":line:"))
                {
                  i++;
                  line++;
                  newLine = true;
                  if(!tokens.get(i).equals("("))
                  {
                    System.out.println("line: "+line + "( is missing");
                    error = true;
                  }
                    i++;
                    if(jobs2.contains(tokens.get(i)))
                    {
                        System.out.println("line : "+line+" job type :" + tokens.get(i)+ " is already defined");
                        error = true;
                    }else
                    {
                        jobs2.add(tokens.get(i));
                    }
                  
                }
                continue;
                }
                if(newLine)
                { 
                 if(tokens.get(i).equals(")")) continue;
                 
                if(tokens.get(i).equals(":line:"))
                {
                  if(tokens.get(i+1).equals("stations")||tokens.get(i+2).equals("stations"))
                  {
                      //to do end the checker
                      if(!tokens.get(i-1).equals(")"))
                      {
                          System.out.println("line: "+line + ") is missing");
                          error = true;
                      }
                      if(!tokens.get(i-2).equals(")"))
                      {
                          System.out.println("line: "+line + ") is missing");
                          error = true;
                      }
                         error(error,"jobs");
                         if(!error)
                         {
                             index2 = i;
                             organizeJobTypes();
                         }
                         break;
                  }
                  newLine = false;
                  if(!tokens.get(i-1).equals(")"))
                  {
                    System.out.println("line: "+line + ") is missing");
                    error = true;
                  }
                  i = i -1;
                  continue;
                }
                 try {
                     float val = (float)Double.parseDouble(tokens.get(i));
                     if(val<0)
                     {
                         System.out.println("line: "+line+ " " + tokens.get(i) + " value of task is smaller than 0 ");
                         error = true;
                     }  
                    } catch (NumberFormatException e) {
                        boolean already = false;
                        
                       for(Task taskName : tasks)
                      {
                        if(taskName.getName().equals(tokens.get(i)))
                        {
                           try {
                                float val = (float)Double.parseDouble(tokens.get(i+1));
                                
                           } catch (Exception x) {
                              
                               if(taskName.getValue()==-1)
                               {
                                   System.out.println("line : " + line + " task : " + tokens.get(i)+ "value is not defined at tasktypes or jobtypes");
                                   error = true;
                               }
                           }
                           already = true;
                                break;
                          }
                        
                          }
                          if(!already)
                          {
                               System.out.println("line : " + line + " task : " + tokens.get(i)+ " is not defined at tasktypes ");
                           error = true; 
                          }
                    }
                
                }
        }
        
    }
    private void StationErrorDetector()
    {
        String stationName = "";
        //start with line 7
        ArrayList<String> stations2 = new ArrayList<String>();
        ArrayList<String> tmpTask = new ArrayList<String>();
        newLine = false;
        index2 = index2 + 1;
        line++;
        
        
                  if(!tokens.get(index2).equals("("))
                  {
                    System.out.println("line: "+line + "( is missing");
                    error = true;
                  }
                  index2++;
                  if(!tokens.get(index2).equals("stations"))
                    {
                        System.out.println("line : "+line+" you write stations keyword wrong or there is no stations");
                        error = true;
                        
                    }
                  stationName = tokens.get(index2);
                  index2++;

        for (int i = index2; i < tokens.size(); i++) {
            
            if(!newLine)
                {  
                if(tokens.get(i).equals(":line:")) // if our code find :line x : type
                {
                  i++;
                  newLine = true;
                  
                    if(!tokens.get(i).equals("("))
                    {
                         System.out.println("line : "+line+"("+ " is not defined");
                         error= true;
                         
                    }else
                    {
                        i++;
                    }
                    if(stations2.contains(tokens.get(i)))
                    {
                        System.out.println("line : "+line+" station type :" + tokens.get(i)+ " is already defined");
                        error = true;
                    }else
                    {
                        stations2.add(tokens.get(i));
                    }
                   i++;
                    try {
                         float val = (float)Double.parseDouble(tokens.get(i));
                         if(val<0)
                         {
                           System.out.println("line : "+line+tokens.get(i)+ " is negative pls enter numbers which bigger than zero");
                           error = true;
                         }
                    } catch (Exception e) {
                        
                        System.out.println("line : "+line+tokens.get(i)+ " is not number pls enter valid maxCapacity");
                        error = true;
                    }
                    i++;
                    
                     try {
                         float val = (float)Double.parseDouble(tokens.get(i));
                         System.out.println("line : "+line+" " +tokens.get(i)+ " is not Y(yes) or N(no) pls enter valid character");
                         error = true;
                        } catch (Exception e) {
                        
                       if(!tokens.get(i).equals("y")&&!tokens.get(i).equals("n"))
                       {
                           System.out.println("line : "+line+" " +tokens.get(i)+ " is not Y(yes) or N(no) pls enter valid character");
                           error = true;
                       }
                     }
                     i++;
                      try {
                         float val = (float)Double.parseDouble(tokens.get(i));
                         System.out.println("line : "+line+" " +tokens.get(i)+ " is not Y(yes) or N(no) pls enter valid character");
                         error = true;
                        } catch (Exception e) {
                        
                       if(!tokens.get(i).equals("y")&&!tokens.get(i).equals("n"))
                       {
                           System.out.println("line : "+line+" " +tokens.get(i)+ " is not Y(yes) or N(no) pls enter valid character");
                           error = true;
                       }
                     }
                      continue;
                }
                }else
            {
                
                 if(tokens.get(i).equals(")")) continue;
                 //to do taskları kontrol et
                if(tokens.get(i).equals(":line:"))
                {
                     line++;
                  if(line==maxLine)
                  {
                      
                      //to do end the checker
                      if(!tokens.get(i-1).equals(")"))
                      {
                          System.out.println("line: "+line + ") is missing");
                          error = true;
                      }
                      if(!tokens.get(i-2).equals(")"))
                      {
                          System.out.println("line: "+line + ") is missing");
                          error = true;
                      }
                         error(error,"Stations");
                         if(!error)
                         {
                             for (JobType job : jobs) {
                                 boolean defined = false;
                                 String text = null;
                                 for (Task task : job.getTasks()) {
                                     if(defined) break;
                                     for (String string : tmpTask) {
                                         if(defined) break;
                                         if(task.getName().equals(string))
                                         {
                                             text = task.getName();
                                             defined = true;
                                             break;
                                         }
                                         
                                         if(defined) break;
                                     }
                                     if(defined) break;
                                 }
                                 if(!defined)
                                     {
                                         error = true;
                                         System.out.println("you defined " + text +" and used in jobs but you did not use");
                                     }
                             }
                             
                             for (Task task : tasks) {
                                 boolean defined = false;
                                
                                 
                                 for (String string : tmpTask) {
                                     if(defined) break;
                                     
                                     if(string.equals(task.getName()))
                                     {
                                         defined = true;
                                         break;
                                     }
                                     if(defined) break;
                                 }
                                     if(!defined) System.out.println(task.getName()+" is defined but you did not use it be carefull");
                                 }
                                 organizeStations();
                             }
                             
                             
                         break;
                  }
                  newLine = false;
                  if(!tokens.get(i-1).equals(")"))
                  {
                    System.out.println("line: "+line + ") is missing");
                    error = true;
                  }
                  i = i -1;
                  continue;
                }
                 try {
                     float val = (float)Double.parseDouble(tokens.get(i));
                     if(val<0)
                     {
                         System.out.println("line: "+line+ " " + tokens.get(i-1) + " value of task is smaller than 0 ");
                         error = true;
                     }  
                    } catch (NumberFormatException e) {
                        boolean already = false;
                        
                       for(Task taskName : tasks)
                      {
                        if(taskName.getName().equals(tokens.get(i)))
                        {
                            tmpTask.add(taskName.getName());
                            already = true;
                            break;
                          }
                        
                          }
                          if(!already)
                          {
                               System.out.println("line : " + line + " task : " + tokens.get(i)+ " is not defined at tasktypes ");
                               error = true;
                          }
                    
                
                }
            }
            
        }
        
    }
    
    private void error(boolean error, String text)
    {
        if(error) System.err.println("Before start the program or see other errors pls fix "+ text + " errors"+
                "\ndo not forget these errors only "+ text+" errors, there may be more errors in other variables");
    }
    
    //*** End of the workflow file methods**//
    
    private void jobOrganizer()
    {
        boolean jobFinder = false;
         for (int i = 0; i < jobTokens.size(); i++) {
            
             if(jobTokens.get(i).equals(":line:")&& !jobFinder)
             {
                 jobFinder = true;
                 i++;
                 jobArrayList.add(new Job(jobTokens.get(i),null,-1,-1));
                 break;
             }
             if(jobFinder)
             {
                 if(jobTokens.get(i).equals(":line:"))
                 {
                     jobFinder = false;
                     i = i - 1;
                 }else
                 {
                     for (JobType job : jobs) {
                         if(job.getName().equals(jobTokens.get(i))) 
                         {
                             jobArrayList.get(jobArrayList.size()-1).setJobType(new JobType(job.getTasks(),job.getName()));
                             i++;
                             break;
                         }
                     }
                     jobArrayList.get(jobArrayList.size()-1).setJobId((float)Double.parseDouble(jobTokens.get(i)));
                     i++;
                     jobArrayList.get(jobArrayList.size()-1).setJobTypeId((float)Double.parseDouble(jobTokens.get(i)));
                     
                 }
             }
             
        }
            
        
    }
}
