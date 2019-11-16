package fpt.dps.dtms.web.rest.vm;

import java.util.List;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import fpt.dps.dtms.service.dto.TMSCustomFieldScreenDTO;

/**
 * View Model object for storing a user's credentials.
 */
public class TMSDynamicCustomFieldVM {
	
	private List<FieldConfigVM> fieldConfigVMs;
	
	private List<TMSCustomFieldScreenDTO> tmsCustomFieldScreenDTOs;
	
	private String processId;

	public List<FieldConfigVM> getFieldConfigVMs() {
		return fieldConfigVMs;
	}

	public void setFieldConfigVMs(List<FieldConfigVM> fieldConfigVMs) {
		this.fieldConfigVMs = fieldConfigVMs;
	}

	public List<TMSCustomFieldScreenDTO> getTmsCustomFieldScreenDTOs() {
		return tmsCustomFieldScreenDTOs;
	}

	public void setTmsCustomFieldScreenDTOs(List<TMSCustomFieldScreenDTO> tmsCustomFieldScreenDTOs) {
		this.tmsCustomFieldScreenDTOs = tmsCustomFieldScreenDTOs;
	}

	public String getProcessId() {
		return processId;
	}

	public void setProcessId(String processId) {
		this.processId = processId;
	}	
}
