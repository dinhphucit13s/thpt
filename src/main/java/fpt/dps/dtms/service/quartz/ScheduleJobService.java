package fpt.dps.dtms.service.quartz;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.scheduling.quartz.QuartzJobBean;

public interface ScheduleJobService {
	boolean scheduleOneTimeJob(String jobName, Class<? extends QuartzJobBean> jobClass, Date date);

	boolean scheduleCronJob(String jobName, Class<? extends QuartzJobBean> jobClass, Date startDate, Date endDate, String groupKey);

	boolean deleteJob(String jobName, String groupKey);

	boolean isJobWithNamePresent(String jobName, String groupKey);

}