package fpt.dps.dtms.web.rest.vm;

import java.time.Instant;

/**
 * View Model object for storing a Logback logger.
 */
public class TMSMonthViewVM {

    private int totalTask = 0;

    private Long totalHours;
    
    private int doneTask = 0;
    
    private int inProcessTask = 0;
    
    private Instant start;
    
    private Instant end;

    public TMSMonthViewVM() {
        // Empty public constructor used by Jackson.
    }

	public int getTotalTask() {
		return totalTask;
	}

	public void setTotalTask(int totalTask) {
		this.totalTask = totalTask;
	}

	public Long getTotalHours() {
		return totalHours;
	}

	public void setTotalHours(Long totalHours) {
		this.totalHours = totalHours;
	}

	public int getDoneTask() {
		return doneTask;
	}

	public void setDoneTask(int doneTask) {
		this.doneTask = doneTask;
	}

	public int getInProcessTask() {
		return inProcessTask;
	}

	public void setInProcessTask(int inProcessTask) {
		this.inProcessTask = inProcessTask;
	}

	public Instant getStart() {
		return start;
	}

	public void setStart(Instant start) {
		this.start = start;
	}

	public Instant getEnd() {
		return end;
	}

	public void setEnd(Instant end) {
		this.end = end;
	}
}
