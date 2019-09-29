package com.example.demo.service.impl;

import com.example.demo.bean.Job;
import com.example.demo.bean.JobState;
import com.example.demo.external.DwhService;
import com.example.demo.external.EmailService;
import com.example.demo.service.JobBusinessService;
import com.example.demo.service.JobService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.MessageFormat;

@Service("jobBusinessService")
public class JobBusinessServiceImpl implements JobBusinessService {
    private static final Logger log = LoggerFactory.getLogger(JobBusinessServiceImpl.class);

    @Resource(name = "jobService")
    private JobService jobService;

    @Override
    public void performJob(Job job) {
        log.info("Receiving job with type {} and priority {}", job.getType(), job.getPriority());

        try {
            runningJob(job);
            callExternalService(job.getType());
            successJob(job);
        } catch (Exception e) {
            log.error("Job error: {}", e.getMessage(), e);
            failJob(job, e.getMessage());
        }

    }

    private void runningJob(Job job) {
        job.setState(JobState.RUNNING);
        jobService.updateJob(job);
    }

    private void successJob(Job job) {
        job.setMessage("OK");
        job.setState(JobState.SUCCESS);
        jobService.updateJob(job);
    }

    private void failJob(Job job, String message) {
        job.setMessage(message);
        job.setState(JobState.FAILED);
        jobService.updateJob(job);
    }

    private void callExternalService(String type) {
        if (type.equalsIgnoreCase("EMAIL")) {
            EmailService.simulateSendingEmail();
        } else if (type.equalsIgnoreCase("DWH")) {
            DwhService.simulateDataLoading();
        } else {
            throw new IllegalArgumentException(MessageFormat.format("Job with type {0} has not configured yet", type));
        }
    }
}
