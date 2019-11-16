package fpt.dps.dtms.domain;

import fpt.dps.dtms.domain.enumeration.BiddingScope;
import fpt.dps.dtms.domain.enumeration.BiddingStatus;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(TaskBidding.class)
public abstract class TaskBidding_ extends fpt.dps.dtms.domain.AbstractAuditingEntity_ {

	public static volatile SingularAttribute<TaskBidding, Tasks> task;
	public static volatile SingularAttribute<TaskBidding, Long> id;
	public static volatile SingularAttribute<TaskBidding, String> biddingRound;
	public static volatile SingularAttribute<TaskBidding, BiddingScope> biddingScope;
	public static volatile SingularAttribute<TaskBidding, BiddingStatus> biddingStatus;

}

