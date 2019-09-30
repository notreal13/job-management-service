package com.example.demo.service.impl;

import com.example.demo.bean.JobSchedule;
import com.example.demo.dao.JobScheduleDao;
import com.example.demo.service.JobScheduleService;
import com.example.demo.service.JobService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.TimeZone;
import java.util.concurrent.ScheduledFuture;

@Service("jobScheduleService")
public class JobScheduleServiceImpl implements JobScheduleService {
    private static final Logger log = LoggerFactory.getLogger(JobScheduleServiceImpl.class);

    private Map<Long, ScheduledFuture<?>> scheduledJobsMap = new HashMap<>();

    @Value("${app.schedule-jobs-from-db.enabled}")
    private boolean scheduleJobsFromDbEnabled;

    @Resource(name = "jobScheduleDao")
    private JobScheduleDao jobScheduleDao;
    @Resource(name = "jobService")
    private JobService jobService;
    @Resource
    private ThreadPoolTaskScheduler taskScheduler;

    @Override
    public Long createJobSchedule(String jobType, Long priority, String cron) {
        Objects.requireNonNull(jobType, "Job type is null");
        Objects.requireNonNull(priority, "Job priority is null");
        Objects.requireNonNull(cron, "Cron expression is null");

        JobSchedule schedule = createNewJobSchedule(jobType, priority, cron);
        schedule.setId(jobScheduleDao.insertJobSchedule(schedule));

        addTaskToTaskScheduler(schedule);

        return schedule.getId();
    }

    @Override
    public JobSchedule getJobSchedule(Long id) {
        Objects.requireNonNull(id, "Job schedule id is null");

        return jobScheduleDao.selectJobSchedule(id);
    }

    @Override
    public void deleteJobSchedule(Long id) {
        Objects.requireNonNull(id, "Job schedule id is null");

        JobSchedule jobSchedule = jobScheduleDao.selectJobSchedule(id);
        if (jobSchedule == null) {
            throw new IllegalArgumentException(MessageFormat.format("Job schedule with id {0} not found", id));
        }

        removeTaskFromScheduler(id);

        jobScheduleDao.deleteJobSchedule(id);
    }

    @PostConstruct
    public void init() {
        if (scheduleJobsFromDbEnabled) {
            List<JobSchedule> jobSchedules = jobScheduleDao.selectJobSchedules();
            if (!CollectionUtils.isEmpty(jobSchedules)) {
                log.info("Found {} job schedules in database. Processing...", jobSchedules.size());
                jobSchedules.forEach(this::addTaskToTaskScheduler);
            }
        } else {
            log.warn("Schedule jobs from DB is disabled by config");
        }
    }

    private JobSchedule createNewJobSchedule(String jobType, Long priority, String cron) {
        JobSchedule schedule = new JobSchedule();
        schedule.setJobType(jobType);
        schedule.setPriority(priority);
        schedule.setCronExpression(cron);
        return schedule;
    }

    private void addTaskToTaskScheduler(JobSchedule schedule) {
        log.info("Adding job with type {} and priority {} to scheduler", schedule.getPriority(), schedule.getPriority());
        ScheduledFuture<?> scheduledFuture = taskScheduler.schedule(
                () -> jobService.createJob(schedule.getJobType(), schedule.getPriority()),
                new CronTrigger(schedule.getCronExpression(), TimeZone.getDefault()));
        scheduledJobsMap.put(schedule.getId(), scheduledFuture);
    }

    private void removeTaskFromScheduler(Long id) {
        log.info("Removing job with id {} from scheduler", id);
        ScheduledFuture<?> scheduledFuture = scheduledJobsMap.get(id);
        if (scheduledFuture != null) {
            scheduledFuture.cancel(false);
            scheduledJobsMap.put(id, null);
        }
    }
}

