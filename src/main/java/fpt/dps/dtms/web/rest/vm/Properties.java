package fpt.dps.dtms.web.rest.vm;

public class Properties {
	private boolean isActiveOnPM;
	private boolean isActiveOnOP;
	private boolean isRequiredOnPM;
	private boolean isRequiredOnOP;
	private boolean isRequiredField;
	private boolean isActivityField;
	private boolean isValidField;
	
	public boolean isActiveOnPM() {
		return isActiveOnPM;
	}
	
	public void setIsActiveOnPM(boolean isActiveOnPM) {
		this.isActiveOnPM = isActiveOnPM;
	}
	
	public boolean getIsActiveOnOP() {
		return isActiveOnOP;
	}
	
	public void setIsActiveOnOP(boolean isActiveOnOP) {
		this.isActiveOnOP = isActiveOnOP;
	}
	
	public boolean getIsRequiredOnPM() {
		return isRequiredOnPM;
	}
	
	public void setIsRequiredOnPM(boolean isRequiredOnPM) {
		this.isRequiredOnPM = isRequiredOnPM;
	}
	
	public boolean getIsRequiredOnOP() {
		return isRequiredOnOP;
	}
	
	public void setIsRequiredOnOP(boolean isRequiredOnOP) {
		this.isRequiredOnOP = isRequiredOnOP;
	}
	
	public boolean getIsRequiredField() {
		return isRequiredField;
	}
	
	public void setIsRequiredField(boolean isRequiredField) {
		this.isRequiredField = isRequiredField;
	}
	
	public boolean getIsActivityField() {
		return isActivityField;
	}
	
	public void setIsActivityField(boolean isActivityField) {
		this.isActivityField = isActivityField;
	}
	
	public boolean getIsValidField() {
		return isValidField;
	}

	public void setIsValidField(boolean isValidField) {
		this.isValidField = isValidField;
	}
}
