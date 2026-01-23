package org.demo.lesson.schedule;

import jakarta.enterprise.context.ApplicationScoped;
import lombok.extern.jbosslog.JBossLog;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import io.quarkus.scheduler.Scheduled;
import io.quarkus.scheduler.Scheduled.ConcurrentExecution;

@ApplicationScoped
@JBossLog
public class TestJob {
    
    // quarkus.scheduler.enabled=false to disable all scheduled jobs, it's default true
    // quarkus.scheduler.every.enabled=false to disable all "every" scheduled jobs
    @Scheduled(
        every = "1m", // simple scheduling
        identity = "JobTest", // assigns a unique job name for logs and metrics, not an anonymous job
        delayed = "1m", // delay first execution by 1 minute after startup
        timeZone = "UTC", // forces UTC regardless of server or container timezone
        //executeWith = Scheduled.QUARTZ, // explicitly use Quartz scheduler engine (default), use if you want additional Quartz features because it requires Quartz dependency
        concurrentExecution = ConcurrentExecution.SKIP) // skip if previous execution still running, prevents overlapping runs
    void execute() {
        log.info("Test job executed: " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss a")));
    }

   @Scheduled(
        cron = "0 2 18 * * ?", // cron-format scheduling @ 6:02 PM every day
        concurrentExecution = ConcurrentExecution.SKIP)
    void process() {
        log.info("Test cron-format job executed: " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss a")));
    }
    /*
        Cron-style means scheduling tasks using a time pattern
        instead of saying "run every x minutes."

        It comes from Unix 'cron', a decades-old job scheduler.

        Quartz/Quarkus cron format

        Cron symbol meanings:
        *  = Every value
        ?  = No specific value (required in day fields)
        ,  = List (e.g. MON,WED,FRI)
        -  = Range (e.g. 1-5)
        /  = Step (e.g. *(slash)10)

        ┌──────── second (0–59)
        │ ┌────── minute (0–59)
        │ │ ┌──── hour (0–23)
        │ │ │ ┌── day of month (1–31)
        │ │ │ │ ┌─ month (1–12 or JAN–DEC)
        │ │ │ │ │ ┌ day of week (1–7 or SUN–SAT)
        │ │ │ │ │ │
        0 0 9 * * ?

        Quartz is a Java scheduling library.
        Quarkus's sceduler uses Quartz-style cron expressions (even though Quarkus itself doesn't require Quartz as a dependency)
    
        Cron-style = time-pattern scheduling (hours, days, weekdays)
        Quartz = Java scheduling library that popularized this cron format
        Quarkus uses Quartz cron syntax in @Scheduled
        Use every for simple intervals, cron for calendar logic
    */
}
