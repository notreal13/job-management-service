package com.example.demo.service;

import com.example.demo.bean.Job;

public interface JobService {
    Long createJob(String type, Long priority);

    void queueJob(Job job);

    void execJobs();

    void updateJob(Job job);

    Job getJob(Long id);
}
