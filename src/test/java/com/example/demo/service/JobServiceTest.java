package com.example.demo.service;

import com.example.demo.bean.Job;
import com.example.demo.bean.Task;
import com.example.demo.service.impl.JobServiceImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.core.task.TaskExecutor;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertArrayEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;

@RunWith(MockitoJUnitRunner.class)
public class JobServiceTest {

    @Mock
    private JobBusinessService jobBusinessServiceMock;
    @Mock
    private TaskExecutor taskExecutorMock;

    @InjectMocks
    private JobServiceImpl jobService;

    @Test
    public void testJobPriority() {
        jobService.queueJob(createJob(3L));
        jobService.queueJob(createJob(2L));
        jobService.queueJob(createJob(1L));

        List<Long> jobPriorities = new ArrayList<>();
        doAnswer(v -> {
            Task task = v.getArgument(0);
            jobPriorities.add(task.getJob().getPriority());
            return null;
        })
                .when(taskExecutorMock)
                .execute(any());
        for (int i = 0; i < 5; i++) {
            jobService.execJobs();
        }

        assertArrayEquals(new Long[]{1L, 2L, 3L}, jobPriorities.toArray());
    }

    private Job createJob(Long priority) {
        Job job = new Job();
        job.setPriority(priority);
        return job;
    }

}