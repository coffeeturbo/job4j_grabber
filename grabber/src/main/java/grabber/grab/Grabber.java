package grabber.grab;

import grabber.Grab;
import grabber.Parse;
import grabber.Store;
import grabber.parse.SqlRuParse;
import grabber.store.PsqlStore;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import static org.quartz.JobBuilder.newJob;
import static org.quartz.SimpleScheduleBuilder.simpleSchedule;
import static org.quartz.TriggerBuilder.newTrigger;

public class Grabber implements Grab {
    private final Properties cfg = new Properties();

    public Store store() {
        try (InputStream in = PsqlStore.class.getClassLoader().getResourceAsStream("app.properties")) {
            Properties config = new Properties();
            config.load(in);
            return (new PsqlStore(config));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Scheduler scheduler() throws SchedulerException {
        Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();
        scheduler.start();
        return scheduler;
    }

    public void cfg() {
        try (InputStream in = Grabber.class.getClassLoader().getResourceAsStream("app.properties")) {
            cfg.load(in);
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    @Override
    public void init(Parse parse, Store store, Scheduler scheduler) throws SchedulerException {
        JobDataMap data = new JobDataMap();
        data.put("store", store);
        data.put("parse", parse);
        JobDetail job = newJob(GrabJob.class)
            .usingJobData(data)
            .build();
        SimpleScheduleBuilder times = simpleSchedule()
            .withIntervalInSeconds(Integer.parseInt(cfg.getProperty("time")))
            .repeatForever();
        Trigger trigger = newTrigger()
            .startNow()
            .withSchedule(times)
            .build();
        scheduler.scheduleJob(job, trigger);
    }

    public static class GrabJob implements Job {

        @Override
        public void execute(JobExecutionContext context) throws JobExecutionException {
            JobDataMap map = context.getJobDetail().getJobDataMap();
            Parse parse = (Parse) map.get("parse");
            Store store = (Store) map.get("store");

            parse.list("https://www.sql.ru/forum/job-offers/").forEach(store::save);
        }
    }

    public static void main(String[] args) throws Exception {
        Grabber grab = new Grabber();
        grab.cfg();
        Scheduler scheduler = grab.scheduler();
        Store store = grab.store();
        grab.init(new SqlRuParse(), store, scheduler);
    }
}
