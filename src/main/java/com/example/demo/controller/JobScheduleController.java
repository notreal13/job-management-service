package com.example.demo.controller;

import com.example.demo.bean.JobSchedule;
import com.example.demo.service.JobScheduleService;
import com.example.demo.util.CommonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.support.CronSequenceGenerator;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.constraints.NotNull;
import java.text.MessageFormat;

@RestController("jobScheduleController")
@RequestMapping("/v1")
public class JobScheduleController {
    private static final Logger log = LoggerFactory.getLogger(JobScheduleController.class);

    @Resource(name = "jobScheduleService")
    private JobScheduleService jobScheduleService;

    @PostMapping(value = "/schedule")
    public ResponseEntity<Long> createJobSchedule(
            @RequestParam(name = "type") String type,
            @RequestParam(name = "priority") Long priority,
            @RequestParam(name = "cron") String cron) {
        log.info("Creating new job schedule with type {}, priority {} and cron {}", type, priority, cron);

        if (!CronSequenceGenerator.isValidExpression(cron)) {
            throw new IllegalArgumentException(MessageFormat.format("Cron expression {0} is invalid", cron));
        }

        Long id = jobScheduleService.createJobSchedule(type, priority, cron);
        return CommonUtil.createResponseEntity(id, MessageFormat.format("Job schedule created with id {0}", id));
    }

    @GetMapping(value = "/schedule/{id}")
    public ResponseEntity<JobSchedule> getJobSchedule(@PathVariable @NotNull Long id) {
        log.info("Looking for a job schedule with id {}", id);
        JobSchedule jobSchedule = jobScheduleService.getJobSchedule(id);
        return CommonUtil.createResponseEntity(jobSchedule, MessageFormat.format("Found job schedule {0}", jobSchedule));
    }

    @DeleteMapping(value = "/schedule/{id}")
    public ResponseEntity deleteJobSchedule(@PathVariable @NotNull Long id) {
        log.info("Deleting job schedule with id {}", id);
        jobScheduleService.deleteJobSchedule(id);
        return CommonUtil.createVoidResultResponse(MessageFormat.format("Job schedule with id {0} deleted", id));
    }
}
