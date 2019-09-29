package com.example.demo.dao;

import com.example.demo.bean.Job;
import com.example.demo.bean.JobState;

import java.util.List;

public interface JobDao {
    Job selectJob(Long id);

    Long insertJob(Job job);

    void updateJob(Job job);

    List<Job> selectJobs(JobState state);
}
