package com.example.demo.bean;

public class JobSchedule {
    private Long id;
    private Long priority;
    private String jobType;
    private String cronExpression;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getPriority() {
        return priority;
    }

    public void setPriority(Long priority) {
        this.priority = priority;
    }

    public String getJobType() {
        return jobType;
    }

    public void setJobType(String jobType) {
        this.jobType = jobType;
    }

    public String getCronExpression() {
        return cronExpression;
    }

    public void setCronExpression(String cronExpression) {
        this.cronExpression = cronExpression;
    }

    @Override
    public String toString() {
        return "JobSchedule{" +
                "id=" + id +
                ", priority=" + priority +
                ", jobType='" + jobType + '\'' +
                ", cronExpression='" + cronExpression + '\'' +
                '}';
    }
}
