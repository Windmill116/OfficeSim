
package Parser;

public class Job {
    
    private JobType jobType;
    private float startTime;
    private float duration;
    private String jobId;

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

    
    
    
    
    
    
}
