
package Parser;
//made by Aykan Ugur all rights belong his coat
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Organizer {
    private boolean typeFinder,typeRead,stationFinder;
   
    private ArrayList<String> tokens; // in
    private ArrayList<Task> tasks = new ArrayList<>();
    private ArrayList<Job> jobs = new ArrayList<>();
    private ArrayList<Station> stations = new ArrayList<>();
    private int index = 0;
    private int index2 = 0;
    private boolean error;
    private int line = 1;
    private boolean newLine = false;
    private boolean firstTime = false;
    private int maxLine;
    private String text2;
    public Organizer(ArrayList<String> tokens,int maxLine) {
        this.tokens = tokens;
        this.maxLine = maxLine;
        taskErrorDetector();
    }
    
    private void organizeTask()
    {
        String ttt = null;
        for (String token : tokens) {
            
                String pattern = "^:[a-zA-Z]+\\s\\d+:";
                Pattern p = Pattern.compile(pattern);
                Matcher m = p.matcher(token);
                if(m.find())
                {
                  continue;
                }
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
                  jobErrorDetector();
                  break; 
               } 
                try {
                            
                     float val = (float)Double.parseDouble(token);
                     tasks.get(tasks.size()-1).setValue(val);
                        
                    } catch (NumberFormatException e) {
                        
                        tasks.add(new Task(token));
                    }
            }
        }
    }
    
    private void organizeJobs()
    {
                String pattern = "^:[a-zA-Z]+\\s\\d+:";
                Pattern p = Pattern.compile(pattern);
                
        
        boolean jobFinder = false;
        index += 3;
        for (int i =index ; i < tokens.size(); i++) {
            Matcher m = p.matcher(tokens.get(i));
                if(m.find())
                {
                  continue;
                }
               
           
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
                  
                   if(tokens.get(i+4).equals("stations")) // çok kırılgan bir kod dikkat!! hata olduğu zaman buraya bak
                   {
                       i = i + 4;
                       index = i;
                       StationErrorDetector();
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
        String pattern = "^:[a-zA-Z]+\\s\\d+:";
        Pattern p = Pattern.compile(pattern);
        
        index++;
        for (int i = index; i < tokens.size(); i++) {
            Matcher m = p.matcher(tokens.get(i));
            if(m.find())
                {
                  continue;
                }
            
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
    public void taskErrorDetector()
    {
        ArrayList<String> tasks2 = new ArrayList<String>();
       if(!tokens.get(1).equals("("))
       {
           System.out.println("line: "+line + "( is missing");
           error = true;
       }
        for (int i = 3; i < tokens.size(); i++) {
            
            String pattern = "^:[a-zA-Z]+\\s\\d+:";
                Pattern p = Pattern.compile(pattern);
                Matcher m = p.matcher(tokens.get(i));
                if(m.find())
                {
                  line++;
                    
                  continue;
                }
            
            if(tokens.get(i).equals("jobtypes"))
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
                index2 = i+1 ;
                
                break;
            }
                try {
                            
                     float val = (float)Double.parseDouble(tokens.get(i));
                     if(val<0)
                     {
                         System.out.println("line: "+line+ " " + tokens.get(i) + " value of task is smaller than 0 ");
                         error = true;
                         continue;
                     }
                     
                        
                    } catch (NumberFormatException e) {
                     
                       if(tasks2.contains(tokens.get(i)))
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
        
        error(error,"task");
        if(!error)
        {
           organizeTask();
        }
    }
    
    private void jobErrorDetector()
    {
         ArrayList<String> jobs2 = new ArrayList<String>();
         String pattern = "^:[a-zA-Z]+\\s\\d+:";
         Pattern p = Pattern.compile(pattern);
        for (int i = index2; i < tokens.size(); i++) {
          
            
                if(!newLine)
                {  
                Matcher m = p.matcher(tokens.get(i));
                if(m.find())
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
                 //to do taskları kontrol et
                Matcher m = p.matcher(tokens.get(i));
                if(m.find())
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
                             organizeJobs();
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
        String pattern = "^:[a-zA-Z]+\\s\\d+:";
        Pattern p = Pattern.compile(pattern);
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
                    
                Matcher m = p.matcher(tokens.get(i));
                if(m.find()) // if our code find :line x : type
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
                Matcher m = p.matcher(tokens.get(i));
                if(m.find())
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
                             for (Job job : jobs) {
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
        if(error) System.err.println("Before start the program or see other errors pls fix "+ text + " errors");
    }
}
