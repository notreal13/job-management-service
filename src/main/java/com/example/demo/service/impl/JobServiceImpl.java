package com.example.demo.service.impl;

import com.example.demo.bean.Job;
import com.example.demo.bean.JobState;
import com.example.demo.bean.Task;
import com.example.demo.dao.JobDao;
import com.example.demo.service.JobBusinessService;
import com.example.demo.service.JobService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.PriorityBlockingQueue;

@Service("jobService")
public class JobServiceImpl implements JobService {
    private static final Logger log = LoggerFactory.getLogger(JobServiceImpl.class);

    private PriorityBlockingQueue<Task> priorityBlockingQueue = new PriorityBlockingQueue<>();

    @Resource(name = "jobDao")
    private JobDao jobDao;
    @Resource(name = "jobBusinessService")
    private JobBusinessService jobBusinessService;
    @Resource(name = "customTaskExecutor")
    private TaskExecutor taskExecutor;

    @Override
    public Job getJob(Long id) {
        Objects.requireNonNull(id, "Job id is null");

        return jobDao.selectJob(id);
    }

    @Override
    public Long createJob(String type, Long priority) {
        Objects.requireNonNull(type, "Job type is null");
        Objects.requireNonNull(priority, "Job priority is null");

        Job job = createNewJob(type, priority);
        job.setId(jobDao.insertJob(job));

        queueJob(job);

        return job.getId();
    }

    private Job createNewJob(String type, Long priority) {
        Job job = new Job();
        job.setType(type);
        job.setPriority(priority);
        job.setState(JobState.QUEUED);
        return job;
    }

    @Override
    public void updateJob(Job job) {
        Objects.requireNonNull(job, "job is null");

        jobDao.updateJob(job);
    }

    @Override
    public void queueJob(Job job) {
        Objects.requireNonNull(job, "Job is null");

        log.info("Adding job to queue {}", job);
        priorityBlockingQueue.put(new Task(jobBusinessService::performJob, job));
    }

    @Scheduled(fixedDelayString = "${app.job-poll-delay-millis}")
    @Override
    public void execJobs() {
        Task task = priorityBlockingQueue.poll();
        if (task != null) {
            log.info("Executing task {}", task);
            taskExecutor.execute(task);
        }
    }

    @PostConstruct
    private void init() {
        List<Job> jobs = jobDao.selectJobs(JobState.QUEUED);
        if (!CollectionUtils.isEmpty(jobs)) {
            log.warn("Found {} queued jobs in database. Processing...", jobs.size());
            jobs.forEach(this::queueJob);
        }
    }

}
