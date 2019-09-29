package com.example.demo.dao;

import com.example.demo.bean.Job;
import com.example.demo.bean.JobState;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest
public class JobDaoTest {

    @Autowired
    private JobDao jobDao;

    @Test
    public void selectJobNonExists() {
        Job job = jobDao.selectJob(ThreadLocalRandom.current().nextLong(100_000, 200_000));
        assertNull(job);
    }

    @Test
    public void insertJob() {
        Job job = createJob();
        assertNull(job.getId());
        Long id = jobDao.insertJob(job);
        assertNotNull(id);
    }

    @Test
    public void selectJob() {
        Job job = createJob();
        Long id = jobDao.insertJob(job);
        Job selectJob = jobDao.selectJob(id);

        assertNotNull(selectJob);
        assertNotNull(selectJob.getId());

        assertEquals(job.getPriority(), selectJob.getPriority());
        assertEquals(job.getType(), selectJob.getType());
        assertEquals(job.getMessage(), selectJob.getMessage());
        assertEquals(job.getState(), selectJob.getState());

        assertNull(job.getCreateTime());
        assertNotNull(selectJob.getCreateTime());

        assertNull(job.getUpdateTime());
        assertNotNull(selectJob.getUpdateTime());
    }

    @Test
    public void updateJob() {
        Job job = createJob();
        Long jobId = jobDao.insertJob(job);

        job.setMessage(UUID.randomUUID().toString());

        Job updatedJob = createJob();
        updatedJob.setId(jobId);
        updatedJob.setState(JobState.SUCCESS);

        jobDao.updateJob(updatedJob);

        Job storedJob = jobDao.selectJob(jobId);

        assertNotNull(storedJob);

        assertEquals(jobId, storedJob.getId());
        assertEquals(job.getPriority(), storedJob.getPriority());
        assertEquals(job.getType(), storedJob.getType());

        assertEquals(updatedJob.getMessage(), storedJob.getMessage());
        assertEquals(updatedJob.getState(), storedJob.getState());

        assertNotNull(storedJob.getCreateTime());
        assertNotNull(storedJob.getUpdateTime());

        assertTrue(storedJob.getUpdateTime().isAfter(storedJob.getCreateTime()));
    }

    private Job createJob() {
        Job job = new Job();
        job.setPriority(ThreadLocalRandom.current().nextLong(100));
        job.setType(UUID.randomUUID().toString().substring(1, 10));
        job.setMessage(UUID.randomUUID().toString());
        job.setState(JobState.QUEUED);
        return job;
    }
}