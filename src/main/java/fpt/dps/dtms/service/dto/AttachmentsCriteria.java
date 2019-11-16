package fpt.dps.dtms.service.dto;

import java.io.Serializable;

import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.LongFilter;

/**
 * Criteria class for the Attachments entity. This class is used in AttachmentsResource to
 * receive all the possible filtering options from the Http GET request parameters.
 * For example the following could be a valid requests:
 * <code> /bugs?id.greaterThan=5&amp;attr1.contains=something&amp;attr2.specified=false</code>
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class AttachmentsCriteria implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private LongFilter id;
	
	private LongFilter bugsId;
	
	public AttachmentsCriteria() {
    }
	
	public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }
    
    public LongFilter getBugsId() {
        return bugsId;
    }

    public void setBugsId(LongFilter bugsId) {
        this.bugsId = bugsId;
    }
    
    @Override
    public String toString() {
        return "AttachmentsCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (bugsId != null ? "bugsId=" + bugsId + ", " : "") +
            "}";
    }

}
