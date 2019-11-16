package fpt.dps.dtms.web.rest.vm.external;

import java.util.List;

import fpt.dps.dtms.web.rest.vm.FieldConfigVM;

public class MultiFieldConfigVM {
	private String target;
	private List<FieldConfigVM> fieldConfigVMs;
	
	public String getTarget() {
		return target;
	}
	public void setTarget(String target) {
		this.target = target;
	}
	public List<FieldConfigVM> getFieldConfigVM() {
		return fieldConfigVMs;
	}
	public void setFieldConfigVM(List<FieldConfigVM> fieldConfigVMs) {
		this.fieldConfigVMs = fieldConfigVMs;
	}
	
	
	public MultiFieldConfigVM() {}
	
	public MultiFieldConfigVM(String target, List<FieldConfigVM> fieldConfigVMs) {
		this.target = target;
		this.fieldConfigVMs = fieldConfigVMs;
	}
	
	
}

