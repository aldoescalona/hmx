/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mx.historicos.api.v2.job;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/**
 *
 * @author Aldo
 */
public class VisitasJob implements Job {

    public VisitasJob() {
    }

    @Override
    public void execute(JobExecutionContext jec) throws JobExecutionException {
        SingleJob job = SingleJob.getInstance();
        job.run();
    }
}
