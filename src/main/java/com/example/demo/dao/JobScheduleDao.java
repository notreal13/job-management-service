package com.example.demo.dao;

import com.example.demo.bean.JobSchedule;

import java.util.List;

public interface JobScheduleDao {
    JobSchedule selectJobSchedule(Long id);

    List<JobSchedule> selectJobSchedules();

    Long insertJobSchedule(JobSchedule jobSchedule);

    void deleteJobSchedule(Long id);
}
