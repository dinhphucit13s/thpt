package fpt.dps.dtms.web.rest.vm;

public class TimeZoneVM {
	
	private String id;
	private String content;
	private int rawOffset;
		
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setTimeZoneId(String timeZoneId) {
		this.id = timeZoneId;
	}
		
	public String getTimeZoneId() {
		return this.id;
	}
		
	public void setTimeZoneContent(String timeZoneContent) {
		this.content = timeZoneContent;
	}
		
	public String getTimeZoneContent() {
		return this.content;
	}
		
	public void setTimeZoneOffset(int offset) {
		this.rawOffset = offset;
	}
		
	public int getTimeZoneOffset() {
		return this.rawOffset;
	}
}
