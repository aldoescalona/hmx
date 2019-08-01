/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.historicos.api.conf;

import java.io.File;
import java.util.Properties;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import mx.historicos.api.bean.Cliente;
import mx.historicos.api.bean.Usuario;
import mx.historicos.api.v2.job.VisitasJob;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.xml.DOMConfigurator;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.Environment;
import org.quartz.CronTrigger;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerFactory;
import org.quartz.impl.StdSchedulerFactory;

/**
 * Web application lifecycle listener.
 *
 * @author 43700118
 */
@WebListener()
public class AppListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        try {

            log4j();

            HmxConfiguration conf = new HmxConfiguration();
            HibernateUtil.addConfiguration(conf, "SMX");
            HibernateUtil.buildSessionFactory("SMX");
            HibernateUtil.setDefaultConfiguration("SMX");

            schedulerCierreMensual();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
    }

    private void log4j() {

        File logdir = new File("/var/smx/logs");
        if (!logdir.exists()) {
            logdir.mkdirs();
        }

        DOMConfigurator.configure(AppListener.class.getResource("/logging.xml"));

        Logger log = Logger.getLogger("org.hibernate");
        log.setLevel(Level.ALL);

    }

    private void schedulerCierreMensual() {

        try {

            SchedulerFactory schedulerFactory = new StdSchedulerFactory();
            Scheduler scheduler = schedulerFactory.getScheduler();

            JobDetail job = new JobDetail("cierre.visita.cron.job", Scheduler.DEFAULT_GROUP, VisitasJob.class);
            CronTrigger cron = new CronTrigger("cierre.visita.cron.cron", Scheduler.DEFAULT_GROUP, "0 0/1 * 1/1 * ? *"); // "5:00 AM"

            scheduler.scheduleJob(job, cron);
            scheduler.start();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
