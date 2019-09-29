package com.example.demo.dao;

import com.example.demo.bean.JobSchedule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest
public class JobScheduleDaoTest {

    @Autowired
    private JobScheduleDao jobScheduleDao;

    @Test
    public void selectJobScheduleNotExists() {
        JobSchedule jobSchedule = jobScheduleDao.selectJobSchedule(ThreadLocalRandom.current().nextLong(100_000, 200_000));
        assertNull(jobSchedule);
    }

    @Test
    public void selectJobSchedule() {
        JobSchedule jobSchedule = createJobSchedule();
        Long id = jobScheduleDao.insertJobSchedule(jobSchedule);
        JobSchedule selectJobSchedule = jobScheduleDao.selectJobSchedule(id);

        assertNotNull(selectJobSchedule);
        assertNotNull(selectJobSchedule.getId());

        assertEquals(jobSchedule.getPriority(), selectJobSchedule.getPriority());
        assertEquals(jobSchedule.getJobType(), selectJobSchedule.getJobType());
        assertEquals(jobSchedule.getCronExpression(), selectJobSchedule.getCronExpression());
    }

    @Test
    public void selectJobSchedules() {
        jobScheduleDao.insertJobSchedule(createJobSchedule());
        jobScheduleDao.insertJobSchedule(createJobSchedule());
        jobScheduleDao.insertJobSchedule(createJobSchedule());

        List<JobSchedule> jobSchedules = jobScheduleDao.selectJobSchedules();
        assertTrue(jobSchedules.size() >= 3);
    }

    @Test
    public void insertJobSchedule() {
        JobSchedule jobSchedule = createJobSchedule();
        assertNull(jobSchedule.getId());
        Long id = jobScheduleDao.insertJobSchedule(jobSchedule);
        assertNotNull(id);
    }

    @Test
    public void deleteJobSchedule() {
        JobSchedule jobSchedule = createJobSchedule();
        Long id = jobScheduleDao.insertJobSchedule(jobSchedule);

        jobScheduleDao.deleteJobSchedule(id);
        JobSchedule selectJobSchedule = jobScheduleDao.selectJobSchedule(id);
        assertNull(selectJobSchedule);

    }

    private JobSchedule createJobSchedule() {
        JobSchedule jobSchedule = new JobSchedule();
        jobSchedule.setPriority(ThreadLocalRandom.current().nextLong(100));
        jobSchedule.setJobType(UUID.randomUUID().toString().substring(1, 10));
        jobSchedule.setCronExpression(UUID.randomUUID().toString().substring(1, 10));
        return jobSchedule;
    }

}