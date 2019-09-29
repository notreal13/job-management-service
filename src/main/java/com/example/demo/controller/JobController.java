package com.example.demo.controller;

import com.example.demo.bean.Job;
import com.example.demo.service.JobService;
import com.example.demo.util.CommonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.constraints.NotNull;
import java.text.MessageFormat;

@RestController("jobController")
@RequestMapping("/v1")
public class JobController {
    private static final Logger log = LoggerFactory.getLogger(JobController.class);

    @Resource(name = "jobService")
    private JobService jobService;

    @PostMapping(value = "/job")
    public ResponseEntity<Long> createJob(
            @RequestParam(name = "type") String type,
            @RequestParam(name = "priority") Long priority) {
        log.info("Creating new job with type {} and priority {}", type, priority);
        Long jobId = jobService.createJob(type, priority);
        return CommonUtil.createResponseEntity(jobId, MessageFormat.format("Job created with id {0}", jobId));
    }

    @GetMapping(value = "/job/{id}")
    public ResponseEntity<Job> getJob(@PathVariable @NotNull Long id) {
        log.info("Looking for a job with id {}", id);
        Job job = jobService.getJob(id);
        return CommonUtil.createResponseEntity(job, MessageFormat.format("Found job {0}", job));
    }
}
