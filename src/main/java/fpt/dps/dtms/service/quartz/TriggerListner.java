package fpt.dps.dtms.service.quartz;

import org.quartz.JobExecutionContext;
import org.quartz.Trigger;
import org.quartz.Trigger.CompletedExecutionInstruction;
import org.quartz.TriggerListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import fpt.dps.dtms.repository.TaskBiddingRepository;
import fpt.dps.dtms.service.CommunicationService;
import fpt.dps.dtms.service.dto.TaskBiddingDTO;
import fpt.dps.dtms.service.mapper.TaskBiddingMapper;

@Component
public class TriggerListner implements TriggerListener {
	
	@Autowired
	TaskBiddingRepository taskBiddingRepository;
	
	@Autowired
	TaskBiddingMapper taskBiddingMapper;

    @Override
    public String getName() {
        return "globalTrigger";
    }

    @Override
    public void triggerFired(Trigger trigger, JobExecutionContext context) {
        //System.out.println("TriggerListner.triggerFired()");
    }

    @Override
    public boolean vetoJobExecution(Trigger trigger, JobExecutionContext context) {
        System.out.println("TriggerListner.vetoJobExecution()");
        return false;
    }

    @Override
    public void triggerMisfired(Trigger trigger) {
        System.out.println("TriggerListner.triggerMisfired()");
        String jobName = trigger.getJobKey().getName();
        System.out.println("Job name: " + jobName + " is misfired");

    }

    @Override
    public void triggerComplete(Trigger trigger, JobExecutionContext context,
            CompletedExecutionInstruction triggerInstructionCode) {
        Long keyId = Long.parseLong(trigger.getJobKey().getName());
        System.out.println("CongHK_test_listener");
        
        TaskBiddingDTO biddingDTO = this.taskBiddingMapper.toDto(this.taskBiddingRepository.findOne(keyId));
        
        CommunicationService.sendReloadHoldingTask(biddingDTO);
    }
}