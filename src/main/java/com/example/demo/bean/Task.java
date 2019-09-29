package com.example.demo.bean;

import java.util.function.Consumer;

public class Task implements Runnable {

    private Consumer<Job> jobConsumer;
    private Job job;

    public Task(Consumer<Job> jobConsumer, Job job) {
        this.jobConsumer = jobConsumer;
        this.job = job;
    }

    public Job getJob() {
        return job;
    }

    @Override
    public void run() {
        jobConsumer.accept(job);
    }

    @Override
    public String toString() {
        return "Task{" +
                "job=" + job +
                '}';
    }
}
