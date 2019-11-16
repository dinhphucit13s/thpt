package fpt.dps.dtms.domain;

import fpt.dps.dtms.domain.enumeration.ErrorSeverity;
import fpt.dps.dtms.domain.enumeration.FIStatus;
import fpt.dps.dtms.domain.enumeration.FixStatus;
import fpt.dps.dtms.domain.enumeration.OPStatus;
import fpt.dps.dtms.domain.enumeration.ReviewStatus;
import fpt.dps.dtms.domain.enumeration.TaskAvailability;
import fpt.dps.dtms.domain.enumeration.TaskPriority;
import fpt.dps.dtms.domain.enumeration.TaskSeverity;
import fpt.dps.dtms.domain.enumeration.TaskStatus;
import java.time.Instant;
import javax.annotation.Generated;
import javax.persistence.metamodel.SetAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Tasks.class)
public abstract class Tasks_ extends fpt.dps.dtms.domain.AbstractAuditingEntity_ {

	public static volatile SingularAttribute<Tasks, Long> parent;
	public static volatile SingularAttribute<Tasks, String> fileName;
	public static volatile SingularAttribute<Tasks, ReviewStatus> review1Status;
	public static volatile SingularAttribute<Tasks, String> fi;
	public static volatile SingularAttribute<Tasks, String> data;
	public static volatile SingularAttribute<Tasks, ErrorSeverity> errorSeverity;
	public static volatile SingularAttribute<Tasks, String> description;
	public static volatile SingularAttribute<Tasks, Integer> actualObject;
	public static volatile SingularAttribute<Tasks, Instant> fiStartTime;
	public static volatile SingularAttribute<Tasks, Integer> errorQuantity;
	public static volatile SingularAttribute<Tasks, TaskAvailability> availability;
	public static volatile SingularAttribute<Tasks, String> type;
	public static volatile SingularAttribute<Tasks, String> fixer;
	public static volatile SingularAttribute<Tasks, FIStatus> fiStatus;
	public static volatile SingularAttribute<Tasks, Instant> fiEndTime;
	public static volatile SingularAttribute<Tasks, Instant> review1StartTime;
	public static volatile SingularAttribute<Tasks, Integer> duration;
	public static volatile SingularAttribute<Tasks, String> review2;
	public static volatile SingularAttribute<Tasks, Instant> review2StartTime;
	public static volatile SingularAttribute<Tasks, String> review1;
	public static volatile SingularAttribute<Tasks, FixStatus> fixStatus;
	public static volatile SingularAttribute<Tasks, Instant> review2EndTime;
	public static volatile SingularAttribute<Tasks, Long> id;
	public static volatile SingularAttribute<Tasks, Instant> review1EndTime;
	public static volatile SetAttribute<Tasks, TMSCustomFieldScreenValue> tmsCustomFieldScreenValues;
	public static volatile SingularAttribute<Tasks, TaskSeverity> severity;
	public static volatile SingularAttribute<Tasks, String> op;
	public static volatile SingularAttribute<Tasks, ReviewStatus> review2Status;
	public static volatile SingularAttribute<Tasks, Instant> estimateEndTime;
	public static volatile SingularAttribute<Tasks, TaskPriority> priority;
	public static volatile SingularAttribute<Tasks, Packages> packages;
	public static volatile SingularAttribute<Tasks, Instant> estimateStartTime;
	public static volatile SingularAttribute<Tasks, Integer> target;
	public static volatile SingularAttribute<Tasks, Instant> fixStartTime;
	public static volatile SingularAttribute<Tasks, String> name;
	public static volatile SingularAttribute<Tasks, OPStatus> opStatus;
	public static volatile SingularAttribute<Tasks, Instant> opStartTime;
	public static volatile SingularAttribute<Tasks, Instant> opEndTime;
	public static volatile SingularAttribute<Tasks, Instant> fixEndTime;
	public static volatile SingularAttribute<Tasks, Integer> frame;
	public static volatile SingularAttribute<Tasks, TaskStatus> status;

}

