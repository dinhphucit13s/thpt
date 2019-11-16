package fpt.dps.dtms.domain;

import fpt.dps.dtms.domain.enumeration.ProjectStatus;
import fpt.dps.dtms.domain.enumeration.ProjectType;
import java.time.Instant;
import javax.annotation.Generated;
import javax.persistence.metamodel.SetAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Projects.class)
public abstract class Projects_ extends fpt.dps.dtms.domain.AbstractAuditingEntity_ {

	public static volatile SingularAttribute<Projects, ProjectUsers> projectLead;
	public static volatile SingularAttribute<Projects, String> code;
	public static volatile SingularAttribute<Projects, BusinessUnit> businessUnit;
	public static volatile SingularAttribute<Projects, String> description;
	public static volatile SetAttribute<Projects, ProjectBugListDefault> projectBugListDefaults;
	public static volatile SingularAttribute<Projects, ProjectType> type;
	public static volatile SingularAttribute<Projects, Integer> biddingHoldTime;
	public static volatile SingularAttribute<Projects, ProjectTemplates> projectTemplates;
	public static volatile SingularAttribute<Projects, String> name;
	public static volatile SingularAttribute<Projects, Instant> startTime;
	public static volatile SingularAttribute<Projects, Long> id;
	public static volatile SingularAttribute<Projects, Instant> endTime;
	public static volatile SetAttribute<Projects, ProjectUsers> projectUsers;
	public static volatile SetAttribute<Projects, PurchaseOrders> purchaseOrders;
	public static volatile SingularAttribute<Projects, ProjectStatus> status;
	public static volatile SingularAttribute<Projects, Customer> customer;

}

