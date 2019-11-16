package fpt.dps.dtms.service.dto;

public class ActivitiTaskDTO {
	private String id;
    private String name;
    private String processInstanceId;

    public ActivitiTaskDTO() {
        super();
    }

    public ActivitiTaskDTO(String id, String name, String processInstanceId) {
        this.id = id;
        this.name = name;
        this.processInstanceId = processInstanceId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProcessInstanceId() {
        return processInstanceId;
    }

    public void setProcessInstanceId(String processInstanceId) {
        this.processInstanceId = processInstanceId;
    }

}
