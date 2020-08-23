package ru.job4j;

import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

import java.io.*;
import java.util.Properties;

import static org.quartz.JobBuilder.*;
import static org.quartz.TriggerBuilder.*;
import static org.quartz.SimpleScheduleBuilder.*;

public class AlertRabbit {
    public static void main(String[] args) {
        try (BufferedReader in =  new BufferedReader(new FileReader("src/main/resources/rabbit.properties"))) {
            Properties config = new Properties();
            config.load(in);
            in.close();

            Connect connect = new Connect(config);
            Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();
            scheduler.start();
            JobDataMap data = new JobDataMap();
            data.put("connect", connect);
            JobDetail job = newJob(Rabbit.class)
                .usingJobData(data)
                .build();
            SimpleScheduleBuilder times = simpleSchedule()
                .withIntervalInSeconds(Integer.parseInt(config.getProperty("rabbit.interval")))
                .repeatForever();
            Trigger trigger = newTrigger()
                .startNow()
                .withSchedule(times)
                .build();
            scheduler.scheduleJob(job, trigger);
            Thread.sleep(10000);
            scheduler.shutdown();

            connect.close();
        } catch (Exception se) {
            se.printStackTrace();
        }
    }

    public static class Rabbit implements Job {

        public Rabbit() {
            System.out.println(hashCode());
        }

        @Override
        public void execute(JobExecutionContext context) throws JobExecutionException {
            System.out.println("Rabbit runs here ...");
            JobDetail job = context.getJobDetail();

            Connect connect = (Connect) job.getJobDataMap().get("connect");
            connect.insert(System.currentTimeMillis());
        }
    }
}
