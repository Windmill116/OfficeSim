package Parser;
import Frontend.EventTemplate;
import java.util.ArrayList;

public class Job {
    
    private JobType jobType;
    private float startTime;
    private float duration;
    private String jobId;
    private float jobTardiness = 0;
    

    ArrayList<Task> tasks = new ArrayList<Task>();
    ArrayList<EventTemplate> eventTemplates = new ArrayList<EventTemplate>();

    public Job(String jobId,JobType jobType, float startTime, float duration) {
        this.jobId = jobId;
        this.jobType = jobType;
        this.startTime = startTime;
        this.duration = duration;
    }

    public JobType getJobType() {
        return jobType;
    }

    public void setJobType(JobType jobType) {
        this.jobType = jobType;
        tasks = jobType.getTasks();
    }

    public float getStartTime() {
        return startTime;
    }

    public void setStartTime(float startTime) {
        this.startTime = startTime;
    }

    public float getDuration() {
        return duration;
    }

    public void setDuration(float duration) {
        this.duration = duration;
    }

    public String getJobId() {
        return jobId;
    }

    public void setJobId(String jobId) {
        this.jobId = jobId;
    }

    public String getName() {
        return jobId;
    }

    public ArrayList<Task> getTasks(){
        return tasks;
    }

    public ArrayList<EventTemplate> getEventTemplates() {
        return eventTemplates;
    }

    public void setTasks(ArrayList<Task> tasks) {
        this.tasks = tasks;
    }

    public void setEventTemplates(ArrayList<EventTemplate> eventTemplates) {
        this.eventTemplates = eventTemplates;
    }

    public void calculateJobTardiness(){
        float jobFinishTime = getDuration() + getStartTime();
        float lastEventTime = getEventTemplates().getLast().getTime();
        jobTardiness = lastEventTime - jobFinishTime;
    }

    public float getJobTardiness() {
        return jobTardiness;
    }

    public void setJobTardiness(float jobTardiness) {
        this.jobTardiness = jobTardiness;
    }
}
