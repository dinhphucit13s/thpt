package fpt.dps.dtms.web.rest.vm.external;

import java.util.HashSet;
import java.util.Set;

import fpt.dps.dtms.service.dto.BugsDTO;
import fpt.dps.dtms.service.dto.TasksDTO;

public class MultiTasksReOpenVM {
	private Set<TasksDTO> listTasksReOpen = new HashSet<>();
	private BugsDTO bugCommon;
	public Set<TasksDTO> getListTasksReOpen() {
		return listTasksReOpen;
	}
	public void setListTasksReOpen(Set<TasksDTO> listTasksReOpen) {
		this.listTasksReOpen = listTasksReOpen;
	}
	public BugsDTO getBugCommon() {
		return bugCommon;
	}
	public void setBugCommon(BugsDTO bugCommon) {
		this.bugCommon = bugCommon;
	}
	
	
}
