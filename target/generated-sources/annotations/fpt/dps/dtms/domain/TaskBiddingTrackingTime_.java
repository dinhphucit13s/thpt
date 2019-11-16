package fpt.dps.dtms.domain;

import fpt.dps.dtms.domain.enumeration.BiddingScope;
import java.time.Instant;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(TaskBiddingTrackingTime.class)
public abstract class TaskBiddingTrackingTime_ extends fpt.dps.dtms.domain.AbstractAuditingEntity_ {

	public static volatile SingularAttribute<TaskBiddingTrackingTime, String> userLogin;
	public static volatile SingularAttribute<TaskBiddingTrackingTime, Integer> duration;
	public static volatile SingularAttribute<TaskBiddingTrackingTime, String> role;
	public static volatile SingularAttribute<TaskBiddingTrackingTime, Tasks> task;
	public static volatile SingularAttribute<TaskBiddingTrackingTime, String> startStatus;
	public static volatile SingularAttribute<TaskBiddingTrackingTime, Instant> startTime;
	public static volatile SingularAttribute<TaskBiddingTrackingTime, Long> id;
	public static volatile SingularAttribute<TaskBiddingTrackingTime, Instant> endTime;
	public static volatile SingularAttribute<TaskBiddingTrackingTime, String> endStatus;
	public static volatile SingularAttribute<TaskBiddingTrackingTime, BiddingScope> biddingScope;

}

