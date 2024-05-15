package Parser;
import java.util.ArrayList;

public class Job {
    
    private JobType jobType;
    private float startTime;
    private float duration;
    private String jobId;
    ArrayList<Task> tasks = new ArrayList<Task>();

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
}
