
package Parser;
import java.util.ArrayList;
public class Organizer {
  private ArrayList<String> tokens; // in data from const
  private ArrayList<Task> tasks = new ArrayList<>(); // define tasks array list
  private ArrayList<JobType> jobs = new ArrayList<>(); // define jobs (jobtype) array list
  private ArrayList<Station> stations = new ArrayList<>(); // define stations array list
  private ArrayList<String> jobTokens; // in data from const
  private ArrayList<Job> jobArrayList = new ArrayList<>();//job array list defined
  private int index = 0, index2 = 0, line = 1, maxLine, parantheses = 0; // int defined
  private boolean newLine = false, error, jobtypesB = false, stationTypesB = false; // booleans defined
  
  public Organizer(ArrayList<String> tokens, int maxLine,ArrayList<String> jobTokens) { // Constructor
    this.tokens = tokens; // for tokens
    this.maxLine = maxLine; // for maxLine 
    this.jobTokens = jobTokens; // for job tokens
    for (String token : tokens) { // checking fatal errors before start the  start                         
      if (token.equals("jobtypes")) {
        jobtypesB = true; // if there is jobtypes string in tokens
      }
      if (token.equals("stations")) {
        stationTypesB = true; // if there is stations string in tokens
      }
      if (jobtypesB && stationTypesB)
        break; // if both of them in tokens break it for optimization
    }
    if (jobtypesB && stationTypesB) // if everything is ok start to check tasks
    {
      taskErrorDetector(); // check task
    } else // if its not ok print whats wrong
    {
      if (!jobtypesB) { // jobtypes is not exist
        System.err.println("**WORKFLOW FILE** "+ "JOBTYPES does not exist. Before starting the program fix it.");
      } else {// stations is not exist
        System.err.println("**WORKFLOW FILE** " + "STATIONS does not exist. Before starting the program fix it.");
      }
    }
  }
  private void organizeTask() // after task error check this code create task objects and send values to the objects
  {
    for (int i = 3; i < tokens.size(); i++) { // for loop to move in tokens
      if (tokens.get(i).equals(":line:")) // ignore lines
        continue;
      if (tokens.get(i).equals(")")) { // check for )
        index = i + 4; // to skip useless tokens
        jobErrorDetector(); // start to check job types
        break; // break to end method (loop)
      } else {
        if (isValid(tokens.get(i))) { // if its start with t it is task
          tasks.add(new Task(tokens.get(i)));
        } else { // if its not start with t it is value
          tasks.getLast().setValue((float) Double.parseDouble(tokens.get(i))); // convert string (from token) to float to send to last task
        }
      }
    }
  }
  private void organizeJobTypes() // after jobs error check this code create job objects and send  values to the objects
  {
      
    for (int i = index; i < tokens.size(); i++) {
        // for loop for move in to tokens
        if(tokens.get(i).equals("("))
        {
            if(!tokens.get(i-1).equals(":line:"))
            {
                tokens.add(i, ":line:");
                index2++;
            }
        }
      if (tokens.get(i).equals("(") || tokens.get(i).equals(")")) // ignore ( and ) tokens
        continue;
      if (tokens.get(i).equals(":line:")) { // to spot line
        if (tokens.get(i + 2).equals("stations")) { // if stations spotted after line
          index = i + 3; // skip useless tokens and
          StationErrorDetector(); // start to check stations errors
          break; // break and stop the method (loop)
        } else { // if its not last line of jobs
          i = i + 2; // skip useless tokens and 
          jobs.add(new JobType(tokens.get(i))); // create new jobtype in job arraylist 
        }
      } else { // if its not line token
        if (isValid(tokens.get(i))) { // if its start with t it is task object
          for (Task task : tasks) { // search for task object
            if (task.getName().equals(tokens.get(i)))
              jobs.getLast().getTasks().add(new Task(task.getName(), task.getValue()));  // when method find the same task object ,
          } // create new task object in last job type object
        } else {
          
          jobs.getLast().getTasks().getLast().setValue((float) Double.parseDouble(tokens.get(i))); // convert token strint to the float to send val
        }
      }
        
    }
  }
  private void organizeStations() // after stations error check , this code create station objects and send values to the that objects
  {
    for (int i = index; i < tokens.size(); i++) {
       
      if (tokens.get(i).equals("(") || tokens.get(i).equals(")")) // ignore ( and ) tokens
        continue;
      if (tokens.get(i).equals(":line:")) { // if token is line
        try {
          tokens.get(i + 4); // check is it end of the workflow file ??
          i = i + 2; // if its not skip it
          String name = tokens.get(i);
          float maxCapacity = (float) Double.parseDouble(tokens.get(i + 1)); // check max capacity
          boolean mutliFlag = false;
          boolean fifoflag = false;
          if (tokens.get(i + 2).equals("y")) // check for multiflag
            mutliFlag = true; // check multiflag
          if (tokens.get(i + 3).equals("y")) // check for fifoflag
            fifoflag = true; // check fifoflag
          stations.add(new Station(name, maxCapacity, mutliFlag, fifoflag, 0, 0)); // creating new object and add to arraylist
          i = i + 3;
        } catch (Exception e) {
          jobFileErrorCheck(); // if stations are end this code starting.
          break;
        }

      } else {
        if (isValid(tokens.get(i))) { // check for is name valid ??
          for (Task task : tasks) { // to find task
            if (task.getName().equals(tokens.get(i))) { 
              stations.getLast().getDefaultTasks().add(new Task(task.getName(), task.getValue())); // add defult task to last stations
              break;
            }
          }
        } else {
          stations.getLast().getDefaultTasks().getLast().setSpeed((float) Double.parseDouble(tokens.get(i))); // set speed
          if (!isValid(tokens.get(i+1))) { // again check for name of task
            i++;
            try {
              stations.getLast().getDefaultTasks().getLast().setPlusMinus((float) Double.parseDouble(tokens.get(i))); // set plus minus
            } catch (Exception e) {
            }
          }
        }
      }
    }
  }
  
  private void taskErrorDetector() // this method check task errors
  {
    ArrayList<String> tasks2 = new ArrayList<>(); // to check same tasks
    if (!tokens.get(1).equals("(")) // check for (
    {
      System.out.println("**WORKFLOW FILE** "+ "line: " + line + "( is missing.");
      error = true;
    }
    if (!tokens.get(2).equals("tasktypes")) // check for tasktypes is wrong or not
    {
      error = true;
      System.out.println("**WORKFLOW FILE** "+ "line: " + line + " You wrote TASKTYPES wrong.");
    }
    for (int i = 3; i < tokens.size(); i++) {
      if (tokens.get(i).equals(":line:")) // ignore :line: tokens
      {
        line++;
        continue;
      }
      if (tokens.get(i).equals("jobtypes")) // to end the code
      {
        if (!tokens.get(i - 1).equals("(")) {
          System.out.println("**WORKFLOW FILE** "+ "line: " + line + " ( is missing.");
          error = true;
        }
        if (!tokens.get(i - 3).equals(")") && !tokens.get(i - 2).equals(")")) {
          System.out.println("**WORKFLOW FILE** " + "line: " + (line - 1) + ") is missing.");
          error = true;
        }
        index2 = i + 1; // to skip useless tokens
        break;
      }
      try {
        float val = (float) Double.parseDouble(tokens.get(i)); // check is it digit
        if (val < 0) {
          System.out.println("**WORKFLOW FILE** "+ "line: " + line + " " + tokens.get(i) + " The value of this task is negative. Please enter a positive task value.");
          error = true; 
        }
      } catch (NumberFormatException e) {
        if (tasks2.contains(tokens.get(i))) // if it is not digit start this code
        {
          error = true;
          System.out.println("**WORKFLOW FILE** "+ "line: " + line + " " + tokens.get(i) + " is already defined.");
        } else {
          if (tokens.get(i).equals("(") || tokens.get(i).equals(")"))
            continue;
          if (isValid(tokens.get(i))) {
            tasks2.add(tokens.get(i));
          } else {
            error = true;
            System.out.println("**WORKFLOW FILE** "+ "Line " + line + " Invalid task type: \"" + tokens.get(i) + "\" Please change the name.");
          }
        }
      }
    }
    error(error, "task"); // start error method
    if (!error) {
      organizeTask(); // if there is no error start organizetask
    }
  }
  private void jobErrorDetector() { // to check job errors
    ArrayList<String> jobs2 = new ArrayList<>();
    for (int i = index2; i < tokens.size(); i++) {
      if (!newLine) {
        if (tokens.get(i).equals(":line:")) { // new line
          i++;
          line++;
          newLine = true;
          if (!tokens.get(i).equals("(")) { // to check (
            System.out.println("**WORKFLOW FILE** "+ "line: " + line + "( is missing.");
            error = true;
          }
          i++;
          if (jobs2.contains(tokens.get(i))) { // to check already defined
            System.out.println("**WORKFLOW FILE** "+ "line: " + line + "   Job type: " + tokens.get(i) + " is already defined.");
            error = true;
          } else {
            if(isValid(tokens.get(i))) // to check valid job type
            {
                jobs2.add(tokens.get(i)); // add new job name
            }else
            {
                error = true; // if name is invalid
                System.out.println("**WORKFLOW FILE** "+ "Line " + line + " Invalid job type: \"" + tokens.get(i) + "\" Please change the name.");
            }
          }
        }
        continue;
      }
      if (newLine) {
        if (tokens.get(i).equals(")")) // ignore )
          continue;

        if (tokens.get(i).equals(":line:")) { // in every line this code is starting
          if (tokens.get(i + 1).equals("stations") || tokens.get(i + 2).equals("stations")) { // to check is it end of the stations
            // to do end the checker
            if (!tokens.get(i - 1).equals(")")) { // to check )
              System.out.println("**WORKFLOW FILE** "+ "line: " + line + ") is missing.");
              error = true;
            }
            if (!tokens.get(i - 2).equals(")")) { // to check )
              System.out.println("**WORKFLOW FILE** "+ "line: " + line + ") is missing.");
              error = true;
            }
            error(error, "jobs"); // to check is there any error if its stop the program
            if (!error) {
              index2 = i;
              organizeJobTypes(); // if there is no error start organizeJobTypes
            }
            break;
          }
          newLine = false;
          if (!tokens.get(i - 1).equals(")")) { // check for )
            System.out.println("**WORKFLOW FILE** "+ "line: " + line + ") is missing.");
            error = true;
          }
          i = i - 1;
          continue;
        }
        try {
          float val = (float) Double.parseDouble(tokens.get(i)); // check for is it number
          if (val < 0) { // if it is check for is it negative ?
            System.out.println("**WORKFLOW FILE** "+ "line: " + line + " " + tokens.get(i) + " The value of this task is negative. Please enter a positive task value. ");
            error = true;
          }
        } catch (NumberFormatException e) {
          boolean already = false;
          for (Task taskName : tasks) {
            if (taskName.getName().equals(tokens.get(i))) { // check is task name defined before tasks
              try {
                float val = (float) Double.parseDouble(tokens.get(i + 1)); // check is it number i+1 (task value) ??

              } catch (Exception x) {
                if (taskName.getValue() == -1) { // check for is task value defined before ??
                  System.out.println("**WORKFLOW FILE** "+ "line: " + line + " task: " + tokens.get(i) + " The value of this task has not been defined in TASKTYPES, or JOBTYPES.");
                  error = true;
                }
              }
              already = true;
              break;
            }
          }
          if (!already) { // check is it defined in tasktypes before ??
            System.out.println("**WORKFLOW FILE** " + "line : " + line + " task : " + tokens.get(i) + " is not defined at tasktypes ");
            error = true;
          }
        }
      }
    }
  }
  private void StationErrorDetector() { // to check station errors
    ArrayList<String> stations2 = new ArrayList<>(); 
    ArrayList<String> tmpTask = new ArrayList<>();
    newLine = false;
    index2 = index2 + 1; // to skip useless tokens
    line++;
    if (!tokens.get(index2).equals("(")) { // to check (
      System.out.println("**WORKFLOW FILE** "+ "line: " + line + "( is missing");
      error = true;
    }
    index2 = index2 + 2;
    for (int i = index2; i < tokens.size(); i++) {
      if (!newLine) {
        if (tokens.get(i).equals(":line:")) // if our code find :line x : type
        {
          i++;
          newLine = true;
          if (!tokens.get(i).equals("(")) { // check for (
            System.out.println("**WORKFLOW FILE** "+ "line : " + line + "(" + " is not defined"); 
            error = true;
          } else {
            i++;
          }
          if (stations2.contains(tokens.get(i))) { // check for already defined
            System.out.println("**WORKFLOW FILE** "+ "line : " + line + " station type :" + tokens.get(i) + " is already defined");
            error = true;
          } else {
            if(isValid(tokens.get(i))) // check for is station name is valid ?
            {
                stations2.add(tokens.get(i)); // if it is add station name to station name list
            }else
            {
            error = true; // if its not valid start this code
            System.out.println("**WORKFLOW FILE** "+ "Line " + line + " Invalid station type: \"" + tokens.get(i) + "\" Please change the name.");
            } 
          }
          i++;
          try {
            float val = (float) Double.parseDouble(tokens.get(i)); // check is it a number?
            if (val < 0) { // if it is check is it negative
              System.out.println("**WORKFLOW FILE** "+ "line : " + line + tokens.get(i) + " The value of this station speed is negative. Please enter a positive speed value.");
              error = true;
            }
          } catch (Exception e) {
            System.out.println("**WORKFLOW FILE** "+ "line : " + line + tokens.get(i) + " is not a number. Please enter a valid maxCapacity value.");
            error = true;
          }
          i++;
          try {
            float val = (float) Double.parseDouble(tokens.get(i));
            System.out.println("**WORKFLOW FILE** "+ "line : " + line + " " + tokens.get(i) + " is not Y(yes) or N(no) it is a number. Please enter a valid character.");
            error = true;
          } catch (Exception e) {
            if (!tokens.get(i).equals("y") && !tokens.get(i).equals("n")) {
              System.out.println("**WORKFLOW FILE** "+ "line : " + line + " " + tokens.get(i) + " is not Y(yes) or N(no). Please enter a valid character.");
              error = true;
            }
          }
          i++;
          try {
            float val = (float) Double.parseDouble(tokens.get(i));
            System.out.println("**WORKFLOW FILE** "+ "line : " + line + " " + tokens.get(i) + " is not Y(yes) or N(no) it is a number. Please enter valid character.");
            error = true;
          } catch (Exception e) {
            if (!tokens.get(i).equals("y") && !tokens.get(i).equals("n")) {
              System.out.println("**WORKFLOW FILE** "+ "line : " + line + " " + tokens.get(i) + " is not Y(yes) or N(no). Please enter valid character.");
              error = true;
            }
          }
        }
      } else {// for end of the station objects check last values
        if (tokens.get(i).equals(")")) // check for )
          continue;
        if (tokens.get(i).equals(":line:")) { // if token is a line line++
          line++;
          if (line == maxLine) {
            if (!tokens.get(i - 1).equals(")")) { // check for )
              System.out.println("**WORKFLOW FILE** "+ "line: " + line + " ) is missing.");
              error = true;
            }
            if (!tokens.get(i - 2).equals(")")) {
              System.out.println("**WORKFLOW FILE** "+ "line: " + line + " ) is missing.");
              error = true;
            }
            error(error, "Station"); // if there is a error end the program
            if (!error) {
                
                for (int j = index; j < tokens.size(); j++) {
                    if(tokens.get(j).equals("("))
                    {
                       if(!tokens.get(j-1).equals(":line:"))
                       {
                           tokens.add(j,":line:");
                           index2++;
                       }
                    }
                }
                for (JobType job : jobs) { // this code check for task defined in jobs and tasktypes
                                                        // but not in stations
                    for (Task task : job.getTasks()) {
                        boolean defined = false;
                        for (String string : tmpTask) {
                            if (defined)
                            break;
                            if(string.equals(task.getName()))
                            {
                                defined = true;
                                break;
                            }
                            if(defined) break;
                        }
                        if (!defined)
                        {
                             error = true;
                             System.out.println("**WORKFLOW FILE** " + "You defined " + task.getName() + " as a task, and used it in job(s), but you did not define which station(s) should be able to complete it.");
                        }
                        
                    }
                } // end of the code
              ArrayList<Task> tmp3 = new ArrayList<>();
              for (Task task : tasks) { // this code check for task defined but not used in anywhere
                boolean defined = false;
                for (String string : tmpTask) {
                  if (defined)
                    break;
                  if (string.equals(task.getName())) {
                    defined = true;
                    break;
                  }
                  if (defined)
                    break;
                }
                if (!defined)
                {
                  System.out.println("**WORKFLOW FILE** " + task.getName() + " is defined, but you did not use it. Be careful.");
                  tmp3.add(task);
                }
              }
              tasks.removeAll(tmp3);
                for (String token : tokens) {
                   if(token.equals("(")||token.equals(")"))  parantheses++;
                }
                if((parantheses%2)>0)
                {
                    error = true;
                    System.out.println("**WORKFLOW FILE** " + " You used too many brackets, this may cause an error. Please fix it before starting the program.");
                }
              organizeStations();
            }
            break;
          }
          newLine = false;
          if (!tokens.get(i - 1).equals(")")) {
            System.out.println("**WORKFLOW FILE** "+ "line: " + line + " ) is missing.");
            error = true;
          }
          i = i - 1;
          continue;
        }
        try { // check for number
          float val = (float) Double.parseDouble(tokens.get(i));
          if (val <= 0) { // if its a number but not positive
            System.out.println("**WORKFLOW FILE** "+ "line: " + line + " "  + " The value of this task is invalid. Please enter a positive or bigger than 0 task value.");
            error = true;
          }
        } catch (NumberFormatException e) {
          boolean already = false; // this code for if you defined task in stations but not in tasktypes

          for (Task taskName : tasks) {
            if (taskName.getName().equals(tokens.get(i))) {
              tmpTask.add(taskName.getName());
              already = true;
              break;
            }
          }
          if (!already) {
            System.out.println("**WORKFLOW FILE** "+ "line: " + line + " task: " + tokens.get(i) + " is not defined in tasktypes. ");
            error = true;
          }
            try {
                float val = (float) Double.parseDouble(tokens.get(i+1)); // this one for speed you HAVE TO DEFINE SPEED
            } catch (Exception a) {                                          // (desing choice)
                
                System.out.println("**WORKFLOW FILE** "+ "line: " + line + " " +tokens.get(i) + " The speed of this task is not defined. Please enter a positive or bigger than 0 task value.");
                error = true;
            }
        }
      }
    }
  }
  private void error(boolean error, String text) {
    if (error) // if there is a error start this code
    {
        System.err.println("Before starting the program please fix " + text + " errors."+ "\nDo not forget these errors are only " + text + " errors, there may be more errors in other variables.");
         System.exit(0); // close the program
    }
      
  }//*** End of the workflow file methods**//
  private void jobOrganizer() { // this code for organize jobs
      for (int i = 0; i < jobTokens.size(); i++) {
         if(jobTokens.get(i).equals(":line:")) //if there is a new line
         {
             i++;
             jobArrayList.add(new Job(jobTokens.get(i),null,-1,-1)); // add new job object to arraylist
         }else
         {
             for (JobType jobType : jobs) {  // to understand which job type  we  defined in job object
                 if(jobType.getName().equals(jobTokens.get(i)))
                 { 
                     jobArrayList.getLast().setJobType(new JobType(jobType.getTasks(),jobType.getName()));
                     i++; // add jobe type to job object (last one in job array)
                     break;
                 }
             }// add other variables
            jobArrayList.get(jobArrayList.size() - 1).setStartTime((float) Double.parseDouble(jobTokens.get(i)));
            i++;
            jobArrayList.get(jobArrayList.size() - 1).setDuration((float) Double.parseDouble(jobTokens.get(i)));
         }  
      }
  }
  private void jobFileErrorCheck() { // to check job file errors
    line = 1; // to reset the line
    ArrayList<String> jobStrings = new ArrayList<>();
    for (int i = 0; i < jobTokens.size(); i++) {
      if (jobTokens.get(i).equals(":line:")) {
        line++; // to increase line
        i++;
        if (!jobStrings.contains(jobTokens.get(i))) {
          if(isValid(jobTokens.get(i)))
          {
              jobStrings.add(jobTokens.get(i));
          }else
          { // if name of the job is not valid 
              error = true;
              System.out.println("**WORKFLOW FILE** "+ "Line " + line + " Invalid job: \"" + jobTokens.get(i) + "\" Please change the name.");
          }
        } else {
          error = true; 
          System.out.println("**JOB FILE** "+ "Line " + line + ": You already defined " + jobTokens.get(i));
        }
        i++;
        Boolean check = false;
        for (JobType jobType : jobs) {
          if (jobType.getName().equals(jobTokens.get(i))) {
            check = true;
            break;
          }
        }
        if (!check) {
          error = true;
          System.out.println("**JOB FILE** "+ "Line " + line + ": You did not define " + jobTokens.get(i) + " in jobtypes.");
        }
      } else {
        try {
          float val = (float) Double.parseDouble(jobTokens.get(i));
          if (val < 0) {
            System.out.println("**JOB FILE** "+ "Line " + line + ": " + jobTokens.get(i) + " is negative.");
            error = true;
          }
        } catch (Exception e) {
          error = true;
          System.out.println("**JOB FILE** "+ "Line " + line + ": " + jobTokens.get(i) + " is not a number.");
        }
      }
    }
    if (!error) {
      jobOrganizer();
    } else {
      error(error, "jobFile");
    }
  }
    public ArrayList<Task> getTasks() {
        return tasks;
    }
    public ArrayList<JobType> getJobTypes() {
        return jobs;
    }
    public ArrayList<Station> getStations() {
        return stations;
    }
    public ArrayList<Job> getJobs() {
        return jobArrayList;
    }
    public static boolean isValid(String str) { // is valid code (check for is names of objects are valid ??)
        return str.matches("^[^0-9].{1,}$"); // our pattern
    }
}
