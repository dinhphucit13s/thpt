package fpt.dps.dtms.service.dto;

import java.io.Serializable;

import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;

/**
 * Criteria class for the Bug entity. This class is used in BugsResource to
 * receive all the possible filtering options from the Http GET request parameters.
 * For example the following could be a valid requests:
 * <code> /bugs?id.greaterThan=5&amp;attr1.contains=something&amp;attr2.specified=false</code>
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class BugsCriteria implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private LongFilter id;
	
	private LongFilter tasksId;
	
	private StringFilter stage;
	
	public BugsCriteria() {
    }
	
	public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }
    
    public LongFilter getTasksId() {
        return tasksId;
    }

    public void setTasksId(LongFilter tasksId) {
        this.tasksId = tasksId;
    }
    
    public StringFilter getStage() {
        return stage;
    }

    public void setStage(StringFilter stage) {
        this.stage = stage;
    }
    
    @Override
    public String toString() {
        return "BugsCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (tasksId != null ? "tasksId=" + tasksId + ", " : "") +
                (stage != null ? "stage=" + stage + ", " : "") +
            "}";
    }

}
