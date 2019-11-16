package fpt.dps.dtms.domain.enumeration;

/**
 * The FixStatus enumeration.
 */
public enum FixStatus {
    NA, 
    OPEN, 
    DOING, 
    PENDING, 
    DONE, 
    CANCEL, 
    REOPEN, 
    RE_ASSIGN, //Re assign task to OP
    RE_ASSIGN_REVIEW1 //Re assign task to Review1
}
