package com.example.demo.service;

import com.example.demo.bean.JobSchedule;

public interface JobScheduleService {
    Long createJobSchedule(String jobType, Long priority, String cron);

    JobSchedule getJobSchedule(Long id);

    void deleteJobSchedule(Long id);
}
