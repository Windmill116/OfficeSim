
package Parser;

public class Job {
    
    private JobType jobType;
    private float jobId;
    private float jobTypeId;
    private String name;

    public Job(String name,JobType jobType, float jobId, float jobTypeId) {
        this.name = name;
        this.jobType = jobType;
        this.jobId = jobId;
        this.jobTypeId = jobTypeId;
    }

    public JobType getJobType() {
        return jobType;
    }

    public void setJobType(JobType jobType) {
        this.jobType = jobType;
    }

    public float getJobId() {
        return jobId;
    }

    public void setJobId(float jobId) {
        this.jobId = jobId;
    }

    public float getJobTypeId() {
        return jobTypeId;
    }

    public void setJobTypeId(float jobTypeId) {
        this.jobTypeId = jobTypeId;
    }
    
    
    
    
    
}
