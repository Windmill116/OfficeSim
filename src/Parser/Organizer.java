
package Parser;
import java.util.ArrayList;
public class Organizer {
    private ArrayList<String> tokens; // in
    private ArrayList<Task> tasks = new ArrayList<>();
    private ArrayList<JobType> jobs = new ArrayList<>();
    private ArrayList<Station> stations = new ArrayList<>();
    private ArrayList<String> jobTokens; // in 
    private ArrayList<Job> jobArrayList = new ArrayList<>();
    private int index = 0,index2 = 0,line = 1,maxLine;
    private boolean newLine = false,error,jobtypesB = false,stationTypesB = false;
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
              System.err.println("**WORKFLOW FILE** " + "jobtypes is not exist and it will cause a lots of problem before start the program fix it");
          }else
          {
               System.err.println("**WORKFLOW FILE** " + "stations is not exist and it will cause a lots of problem before start the program fix it");
          }
      }
    }
    private void organizeTask() // after task error check this code create task objects and send values to the objects
    {
        for (int i = 3; i < tokens.size(); i++) {
            if(tokens.get(i).equals(":line:")) continue;
            
            if(tokens.get(i).equals(")"))
            {
                index = i+4;
                jobErrorDetector();
                break;
            }else
            {
                if(tokens.get(i).startsWith("t"))
                {
                    tasks.add(new Task(tokens.get(i)));
                }else
                {
                    tasks.getLast().setValue((float)Double.parseDouble(tokens.get(i)));
                }
            }
        }
    }
    private void organizeJobTypes() // after jobs error check this code create job objects and send  values to the objects
    {
        for (int i = index; i < tokens.size(); i++) {
            if(tokens.get(i).equals("(")||tokens.get(i).equals(")")) continue;
            
            if(tokens.get(i).equals(":line:"))
            {
               if(tokens.get(i+2).equals("stations"))
               {
                   index = i + 3;
                   StationErrorDetector();
                   break;
               }else
               {
                    i = i + 2;
                    jobs.add(new JobType(tokens.get(i)));
               }
                
            }else
            {
                if(tokens.get(i).startsWith("t"))
                {
                    for (Task task : tasks) {
                        if(task.getName().equals(tokens.get(i))) jobs.getLast().getTasks().add(new Task(task.getName(),task.getValue()));
                    }
                }else
                {
                    
                    jobs.getLast().getTasks().getLast().setValue((float)Double.parseDouble(tokens.get(i)));
                }
            }
            
            
        }
    }
    private void organizeStations() // after stations error check , this code create station objects and send values to the that objects
    {
        for (int i = index; i < tokens.size(); i++) {
            if(tokens.get(i).equals("(")||tokens.get(i).equals(")")) continue;
            if(tokens.get(i).equals(":line:"))
            {
                try {
                tokens.get(i+4);
                i = i + 2;
                String name = tokens.get(i);
                float maxCapacity = (float)Double.parseDouble(tokens.get(i+1)); // check max capacity
                boolean mutliFlag = false;
                boolean fifoflag = false;
                if(tokens.get(i+2).equals("Y")) mutliFlag = true; // check multiflag
                if(tokens.get(i+3).equals("Y")) fifoflag = true; // check fifoflag
                stations.add(new Station(name,maxCapacity,mutliFlag,fifoflag,0,0));
                i = i + 3;
                } catch (Exception e) {
                   jobFileErrorCheck();
                    break;
                }
                
            }else
            {
                
                if(tokens.get(i).startsWith("t"))
                {
                    for (Task task : tasks) {
                        if(task.getName().equals(tokens.get(i)))
                        {
                           
                            stations.getLast().getDefaultTasks().add(new Task(task.getName(),task.getValue()));
                            break;
                        }
                    }
                }else
                {
                  
                   stations.getLast().getDefaultTasks().getLast().setSpeed((float)Double.parseDouble(tokens.get(i)));
                   if(!tokens.get(i+1).startsWith("t"))
                   {
                       i++;
                       try {
                           stations.getLast().getDefaultTasks().getLast().setPlusMinus((float)Double.parseDouble(tokens.get(i)));
                       } catch (Exception e) {
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
    public ArrayList<JobType> getJobTypes() {
        return jobs;
    }
    public ArrayList<Station> getStations() {
        return stations;
    }

    public ArrayList<Job> getJobArrayList() {
        return jobArrayList;
    }
    //*** END OF GET AND SET OBJECTS***
    private void taskErrorDetector() // this method check task errors
    {
        ArrayList<String> tasks2 = new ArrayList<>(); //to check same tasks
       if(!tokens.get(1).equals("("))// check for (
       {
           System.out.println("**WORKFLOW FILE** " + "line: "+line + "( is missing");
           error = true;
       }
       if(!tokens.get(2).equals("tasktypes")) // check for tasktypes is wrong or not
       {
           error = true;
           System.out.println("**WORKFLOW FILE** " + "line: " + line + " you wrote tasktypes wrong");
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
                    System.out.println("**WORKFLOW FILE** " + "line: "+line + " ( is missing"); 
                    error = true;
                }
                if(!tokens.get(i-3).equals(")")&&!tokens.get(i-2).equals(")"))
                {
                   System.out.println("**WORKFLOW FILE** " + "line: " +(line-1)+ ") is missing"); 
                   error = true;
                }
                index2 = i+1 ; // to skip useless tokens
                break;
            }
                try {      
                     float val = (float)Double.parseDouble(tokens.get(i)); // check is it digit 
                     if(val<0)
                     {
                         System.out.println("**WORKFLOW FILE** " + "line: "+line+ " " + tokens.get(i) + " value of task is smaller than 0 ");
                         error = true;
                     }
                    } catch (NumberFormatException e) {
                     
                       if(tasks2.contains(tokens.get(i))) // if it is not digit start this code
                       {
                           error = true;
                           System.out.println("**WORKFLOW FILE** " + "line: "+line+ " " + tokens.get(i) + " is already defined");
                       }else
                       {
                           if(tokens.get(i).equals("(")||tokens.get(i).equals(")")) continue;
                           if(tokens.get(i).startsWith("t"))
                           {
                               tasks2.add(tokens.get(i));
                           }else
                           {
                               error = true;
                               System.out.println("**WORKFLOW FILE** " + "Line " + line + " this task type is invalid \"" + tokens.get(i) + "\" pls change the name" );
                           }
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
         ArrayList<String> jobs2 = new ArrayList<>();
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
                    System.out.println("**WORKFLOW FILE** " + "line: "+line + "( is missing");
                    error = true;
                  }
                    i++;
                    if(jobs2.contains(tokens.get(i)))
                    {
                        System.out.println("**WORKFLOW FILE** " + "line : "+line+" job type :" + tokens.get(i)+ " is already defined");
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
                          System.out.println("**WORKFLOW FILE** " + "line: "+line + ") is missing");
                          error = true;
                      }
                      if(!tokens.get(i-2).equals(")"))
                      {
                          System.out.println("**WORKFLOW FILE** " + "line: "+line + ") is missing");
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
                    System.out.println("**WORKFLOW FILE** " + "line: "+line + ") is missing");
                    error = true;
                  }
                  i = i -1;
                  continue;
                }
                 try {
                     float val = (float)Double.parseDouble(tokens.get(i));
                     if(val<0)
                     {
                         System.out.println("**WORKFLOW FILE** " + "line: "+line+ " " + tokens.get(i) + " value of task is smaller than 0 ");
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
                                   System.out.println("**WORKFLOW FILE** " + "line : " + line + " task : " + tokens.get(i)+ "value is not defined at tasktypes or jobtypes");
                                   error = true;
                               }
                           }
                           already = true;
                                break;
                          }
                        
                          }
                          if(!already)
                          {
                               System.out.println("**WORKFLOW FILE** " + "line : " + line + " task : " + tokens.get(i)+ " is not defined at tasktypes ");
                           error = true; 
                          }
                    }
                
                }
        }
        
    }
    private void StationErrorDetector()
    {
        //start with line 7
        ArrayList<String> stations2 = new ArrayList<String>();
        ArrayList<String> tmpTask = new ArrayList<String>();
        newLine = false;
        index2 = index2 + 1;
        line++;
        
        
                  if(!tokens.get(index2).equals("("))
                  {
                    System.out.println("**WORKFLOW FILE** " + "line: "+line + "( is missing");
                    error = true;
                  }
                  index2++;
                  if(!tokens.get(index2).equals("stations"))
                    {
                        System.out.println("**WORKFLOW FILE** " + "line : "+line+" you write stations keyword wrong or there is no stations");
                        error = true;
                        
                    }
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
                         System.out.println("**WORKFLOW FILE** " + "line : "+line+"("+ " is not defined");
                         error= true;
                         
                    }else
                    {
                        i++;
                    }
                    if(stations2.contains(tokens.get(i)))
                    {
                        System.out.println("**WORKFLOW FILE** " + "line : "+line+" station type :" + tokens.get(i)+ " is already defined");
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
                           System.out.println("**WORKFLOW FILE** " + "line : "+line+tokens.get(i)+ " is negative pls enter numbers which bigger than zero");
                           error = true;
                         }
                    } catch (Exception e) {
                        
                        System.out.println("**WORKFLOW FILE** " + "line : "+line+tokens.get(i)+ " is not number pls enter valid maxCapacity");
                        error = true;
                    }
                    i++;
                    
                     try {
                         float val = (float)Double.parseDouble(tokens.get(i));
                         System.out.println("**WORKFLOW FILE** " + "line : "+line+" " +tokens.get(i)+ " is not Y(yes) or N(no) pls enter valid character");
                         error = true;
                        } catch (Exception e) {
                        
                       if(!tokens.get(i).equals("y")&&!tokens.get(i).equals("n"))
                       {
                           System.out.println("**WORKFLOW FILE** " + "line : "+line+" " +tokens.get(i)+ " is not Y(yes) or N(no) pls enter valid character");
                           error = true;
                       }
                     }
                     i++;
                      try {
                         float val = (float)Double.parseDouble(tokens.get(i));
                         System.out.println("**WORKFLOW FILE** " + "line : "+line+" " +tokens.get(i)+ " is not Y(yes) or N(no) pls enter valid character");
                         error = true;
                        } catch (Exception e) {
                        
                       if(!tokens.get(i).equals("y")&&!tokens.get(i).equals("n"))
                       {
                           System.out.println("**WORKFLOW FILE** " + "line : "+line+" " +tokens.get(i)+ " is not Y(yes) or N(no) pls enter valid character");
                           error = true;
                       }
                     }
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
                          System.out.println("**WORKFLOW FILE** " + "line: "+line + ") is missing");
                          error = true;
                      }
                      if(!tokens.get(i-2).equals(")"))
                      {
                          System.out.println("**WORKFLOW FILE** " + "line: "+line + ") is missing");
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
                                         System.out.println("**WORKFLOW FILE** " + "you defined " + text +" and used in jobs but you did not use");
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
                                     if(!defined) System.out.println("**WORKFLOW FILE** " + task.getName()+" is defined but you did not use it be careful");
                                 }
                                 organizeStations();
                             }
                         break;
                  }
                  newLine = false;
                  if(!tokens.get(i-1).equals(")"))
                  {
                    System.out.println("**WORKFLOW FILE** " + "line: "+line + ") is missing");
                    error = true;
                  }
                  i = i -1;
                  continue;
                }
                 try {
                     float val = (float)Double.parseDouble(tokens.get(i));
                     if(val<0)
                     {
                         System.out.println("**WORKFLOW FILE** " + "line: "+line+ " " + tokens.get(i-1) + " value of task is smaller than 0 ");
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
                               System.out.println("**WORKFLOW FILE** " + "line : " + line + " task : " + tokens.get(i)+ " is not defined at tasktypes ");
                               error = true;
                          }
                }
            }
            
        }
        
    }
    
    private void error(boolean error, String text)
    {
        if(error) System.err.println("Before start the program or see other errors pls fix "+ text + " errors"+"\ndo not forget these errors only "+ text+" errors, there may be more errors in other variables");      
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
                     jobArrayList.get(jobArrayList.size()-1).setStartTime((float)Double.parseDouble(jobTokens.get(i)));
                     i++;
                     jobArrayList.get(jobArrayList.size()-1).setDuration((float)Double.parseDouble(jobTokens.get(i)));
                 }
             }
        }
    }
    private void jobFileErrorCheck()
    {
        line = 1;
        ArrayList<String> jobStrings = new ArrayList<>();
        for (int i = 0; i < jobTokens.size(); i++) {
            if(jobTokens.get(i).equals(":line:"))
            {
                line++;
                i++;
                if(!jobStrings.contains(jobTokens.get(i)))
                {
                    jobStrings.add(jobTokens.get(i));
                }else
                {
                    error = true;
                    System.out.println("**JOB FILE** " + "Line "+ line + ": you already defined " + jobTokens.get(i));
                }
                i++;
                Boolean check = false;
                for (JobType jobType :jobs ) {
                    if(jobType.getName().equals(jobTokens.get(i)))
                    {
                        check = true;
                        break;
                    }
                }
                if(!check)
                {
                    error = true;
                     System.out.println("**JOB FILE** " + "Line "+ line + ": you did not define " + jobTokens.get(i) + " in jobtypes");
                }
            }else
            {
                try {
                    float val =  (float)Double.parseDouble(jobTokens.get(i));
                    if(val<0)
                    {
                         System.out.println("**JOB FILE** " + "Line "+ line + ": "+ jobTokens.get(i) + " is negative");
                         error = true;
                    }
                } catch (Exception e) {
                    
                    error = true;
                     System.out.println("**JOB FILE** " + "Line "+ line +": " + jobTokens.get(i) + " is not number");
                }
            } 
        }
        if(!error)
        {
            jobOrganizer();
        }else
        {
            error(error,"jobFile");
        }
    }
}
