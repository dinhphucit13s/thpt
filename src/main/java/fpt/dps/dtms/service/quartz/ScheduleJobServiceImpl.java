package fpt.dps.dtms.service.quartz;

import java.util.Date;

import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.stereotype.Service;

@Service
public class ScheduleJobServiceImpl implements ScheduleJobService {

	@Autowired
	@Lazy
	SchedulerFactoryBean schedulerFactoryBean;

	@Autowired
	private ApplicationContext context;

	@Override
	public boolean scheduleOneTimeJob(String jobName, Class<? extends QuartzJobBean> jobClass, Date date) {
		return false;
	}

	@Override
	public boolean scheduleCronJob(String jobName, Class<? extends QuartzJobBean> jobClass, Date startDate, Date endDate,
			String groupKey) {
		System.out.println("Request received to scheduleJob");

		String jobKey = jobName;
		String triggerKey = jobName;

		JobDetail jobDetail = ScheduleJobUtil.createJob(jobClass, true, context, jobKey, groupKey);

		System.out.println("creating trigger for key :" + jobKey + " at date :" + startDate);

		Trigger cronTriggerBean = ScheduleJobUtil.createCronTrigger(triggerKey, startDate, endDate);

		try {			

				Scheduler scheduler = schedulerFactoryBean.getScheduler();
				Date dt = scheduler.scheduleJob(jobDetail, cronTriggerBean);

				System.out.println("Job with key jobKey :" + jobKey + " and group :" + groupKey
						+ " scheduled successfully for date :" + dt);
			return true;
		} catch (SchedulerException e) {
			System.out.println(
					"SchedulerException while scheduling job with key :" + jobKey + " message :" + e.getMessage());
			e.printStackTrace();
		}

		return false;
	}

	@Override
	public boolean deleteJob(String jobName, String groupKey) {
		System.out.println("Request received for deleting job.");
		if (!isJobWithNamePresent(jobName, groupKey)) {
			return true;
		}

		String jobKey = jobName;
		JobKey jkey = new JobKey(jobKey, groupKey);
		System.out.println("Parameters received for deleting job : jobKey :" + jobKey);

		try {
			boolean status = schedulerFactoryBean.getScheduler().deleteJob(jkey);
			System.out.println("Job with jobKey :" + jobKey + " deleted with status :" + status);
			return status;
		} catch (SchedulerException e) {
			System.out.println(
					"SchedulerException while deleting job with key :" + jobKey + " message :" + e.getMessage());
			e.printStackTrace();
			return false;
		}
	}
	
	/**
	 * Check job exist with given name
	 */
	@Override
	public boolean isJobWithNamePresent(String jobName, String groupKey) {
		try {
			JobKey jobKey = new JobKey(jobName, groupKey);
			Scheduler scheduler = schedulerFactoryBean.getScheduler();
			if (scheduler.checkExists(jobKey)) {
				return true;
			}
		} catch (SchedulerException e) {
			System.out.println("SchedulerException while checking job with name and group exist:" + e.getMessage());
			e.printStackTrace();
		}
		return false;
	}
}
