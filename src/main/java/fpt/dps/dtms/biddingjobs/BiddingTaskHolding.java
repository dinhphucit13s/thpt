package fpt.dps.dtms.biddingjobs;

import java.util.Date;

import org.quartz.DisallowConcurrentExecution;
import org.quartz.InterruptableJob;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.JobKey;
import org.quartz.UnableToInterruptJobException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;

import fpt.dps.dtms.domain.TaskBidding;
import fpt.dps.dtms.domain.Tasks;
import fpt.dps.dtms.domain.enumeration.BiddingStatus;
import fpt.dps.dtms.domain.enumeration.FIStatus;
import fpt.dps.dtms.domain.enumeration.FixStatus;
import fpt.dps.dtms.domain.enumeration.OPStatus;
import fpt.dps.dtms.domain.enumeration.ReviewStatus;
import fpt.dps.dtms.repository.TaskBiddingRepository;
import fpt.dps.dtms.repository.TasksRepository;
import fpt.dps.dtms.repository.search.TaskBiddingSearchRepository;
import fpt.dps.dtms.repository.search.TasksSearchRepository;
import fpt.dps.dtms.service.quartz.ScheduleJobService;

@DisallowConcurrentExecution
public class BiddingTaskHolding extends QuartzJobBean implements InterruptableJob{

	private volatile boolean toStopFlag = true;
	
	private final Logger log = LoggerFactory.getLogger(BiddingTaskHolding.class);
	
	@Autowired
	TaskBiddingRepository taskBiddingRepository;
	
	@Autowired
	TaskBiddingSearchRepository taskBiddingSearchRepository;
	
	@Autowired
	TasksRepository tasksRepository;
	
	@Autowired
	TasksSearchRepository tasksSearchRepository;
	
	@Autowired
	ScheduleJobService scheduleJobService;
	
	@Override
	public void interrupt() throws UnableToInterruptJobException {
		System.out.println("Stopping thread... ");
		log.debug("Cron Job[Create Task From Schedule] Stopping...");
		toStopFlag = false;
		
	}

	@Override
	protected void executeInternal(JobExecutionContext jobExecutionContext) throws JobExecutionException {
		JobKey key = jobExecutionContext.getJobDetail().getKey();

		Long keyId = Long.parseLong(key.getName());
		String step = key.getGroup();
		
		TaskBidding taskBidding = this.taskBiddingRepository.findOne(keyId);
		if (taskBidding != null) {
			if (taskBidding.getBiddingStatus().equals(BiddingStatus.HOLDING)) {
				taskBidding.setBiddingStatus(BiddingStatus.NA);
				taskBidding = this.rollbackStepPic(taskBidding, step);
				taskBidding = this.taskBiddingRepository.save(taskBidding);
				this.taskBiddingSearchRepository.save(taskBidding);
			}
		}
		scheduleJobService.deleteJob(key.getName(), step);
	}
	
	private TaskBidding rollbackStepPic(TaskBidding taskBidding, String step) {
		Tasks tasksDomain = taskBidding.getTask();
		switch (step) {
		case "review1":
			tasksDomain.setReview1(null);
			tasksDomain.setReview1Status(ReviewStatus.NA);
			break;
		case "review2":
			tasksDomain.setReview2(null);
			tasksDomain.setReview2Status(ReviewStatus.NA);
			break;
		case "fi":
			tasksDomain.setFi(null);
			tasksDomain.setFiStatus(FIStatus.NA);
			break;
		case "fixer":
			tasksDomain.setFixer(null);
			tasksDomain.setFixStatus(FixStatus.NA);
			break;
		default:
			tasksDomain.setOp(null);
			tasksDomain.setOpStatus(OPStatus.NA);
			break;
		}
		tasksDomain = this.tasksRepository.save(tasksDomain);
		this.tasksSearchRepository.save(tasksDomain);
		taskBidding.setTask(tasksDomain);
		
		return taskBidding;
	}

}