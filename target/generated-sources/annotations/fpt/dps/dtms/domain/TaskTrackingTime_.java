package fpt.dps.dtms.domain;

import java.time.Instant;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(TaskTrackingTime.class)
public abstract class TaskTrackingTime_ extends fpt.dps.dtms.domain.AbstractAuditingEntity_ {

	public static volatile SingularAttribute<TaskTrackingTime, String> userLogin;
	public static volatile SingularAttribute<TaskTrackingTime, Integer> duration;
	public static volatile SingularAttribute<TaskTrackingTime, String> role;
	public static volatile SingularAttribute<TaskTrackingTime, String> startStatus;
	public static volatile SingularAttribute<TaskTrackingTime, Instant> startTime;
	public static volatile SingularAttribute<TaskTrackingTime, Long> id;
	public static volatile SingularAttribute<TaskTrackingTime, Instant> endTime;
	public static volatile SingularAttribute<TaskTrackingTime, Long> taskId;
	public static volatile SingularAttribute<TaskTrackingTime, String> endStatus;

}

