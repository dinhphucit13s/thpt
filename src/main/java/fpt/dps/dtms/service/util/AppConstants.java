package fpt.dps.dtms.service.util;

import java.time.ZoneId;

public interface AppConstants
{
	public static final String DEFAULT_PASSWORD = "1234";
    public static final String PACKAGE_ENTITY = "Package";
    public static final String TASK_ENTITY = "Task";
    public static final String NODE_DESCRIPTION = "documentation";
    public static final String NODE_TYPE = "type";
    public static final String ROUND_OP = "op";
    public static final String ROUND_REVIEW1 = "review1";
    public static final String ROUND_REVIEW2 = "review2";
    public static final String ROUND_FIXER = "fixer";
    public static final String ROUND_FI = "fi";
    public static final String PENDING = "PENDING";
    public static final String DOING = "DOING";
    public static final String DONE = "DONE";
    public static final String NOT_GOOD = "NOT_GOOD";
    public static final String RE_ASSIGN = "RE_ASSIGN";
    public static final String RE_ASSIGN_REVIEW1 = "RE_ASSIGN_REVIEW1";
    public static final String END = "end";
    public static final String REOPEN = "REOPEN";
    public static final String OPEN = "OPEN";
    public static final String NA = "NA";
    public static final String TASK_CANCEL = "TASK_CANCEL";
    public static final String TASK_BIDDING = "BIDDING";
    
    //Define notification message
    public static final String MESSAGE_OPEN = "opened";
    public static final String MESSAGE_REOPEN = "re-opened";
    public static final String MESSAGE_CANCEL = "cancelled";
    
    //Define exception case
    public static final String IN_USED = "inUsed";
    public static final String IMPORT_FAILED = "importFailed";
    public static final String NOT_FOUND = "Not found ";
    
    //Define system zone.
    public static final ZoneId SYSTEM_ZONE_ID = ZoneId.systemDefault(); 
    public static final ZoneId SYSTEM_ZONE_0 = ZoneId.of("+0"); 
    public static final String DATETIME_PATTERN = "d/MM/yyyy"; 
    
    /*DEFINE PROPERTIES FOR ONLY TOYOTA PROJECT*/
    public static final String TOYOTA_FILE_NAME = "TOYOTA_TASK_LIST";
    public static final String PACKAGE_COLUMN_NAME = "PROJECT_TITLE";
    public static final String TASK_COLUMN_NAME = "TASK_ID";
    public static final String PHASE_COLUMN_NAME = "PHASE";
    public static final String TASK_EST_START_TIME_COLUMN_NAME = "STARTED_DATETIME";
    public static final String TASK_EST_END_TIME_COLUMN_NAME = "UPDATED_DATETIME";
    public static final String TASK_FRAME = "INPUT_DATA_COUNT";
    //public static final String TASK_OP_COLUMN_NAME = "FIRST_ANNOTATION_USER_ID";
    public static final String TASK_OP_COLUMN_NAME = "ANNOTATION_USERNAME";
    public static final String TASK_OP_ACTUAL_START_TIME_COLUMN_NAME = "FIRST_ANNOTATION_STARTED_DATETIME";
    public static final String TASK_OP_ACTUAL_END_TIME_COLUMN_NAME = "FIRST_ANNOTATION_ENDED_DATETIME";
    public static final String TASK_OP_ACTUAL_DURATION_COLUMN_NAME = "ANNOTATION_WORKTIME_HOUR";
    public static final String TASK_REVIEW_COLUMN_NAME = "INSPECTION_USERNAME";
    public static final String TASK_REVIEW1_COLUMN_NAME = "INSPECTION_USERNAME_%s_00";
    public static final String TASK_REVIEW_DURATION_COLUMN_NAME = "INSPECTION_WORKTIME_HOUR";
    public static final String TASK_REVIEW1_DURATION_COLUMN_NAME = "INSPECTION_WORKTIME_HOUR_%s_00";
    public static final String TASK_REVIEW2_COLUMN_NAME = "INSPECTION_USERNAME_%s_01";
    public static final String TASK_REVIEW2_DURATION_COLUMN_NAME = "INSPECTION_WORKTIME_HOUR_%s_01";
    //Start from 02 to n will assign to FI
    public static final String TASK_FI_COLUMN_NAME = "INSPECTION_USERNAME_%s_02"; 
    public static final String TASK_FI_DURATION_COLUMN_NAME = "INSPECTION_WORKTIME_HOUR_%s_0";
    public static final String TASK_DURATION = "duration";
    
    public static final float DEFAULT_EFFORT_PLAN = 8;
    
    
}
