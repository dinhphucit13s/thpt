package fpt.dps.dtms.domain;

import fpt.dps.dtms.domain.enumeration.BugResolution;
import fpt.dps.dtms.domain.enumeration.BugStatus;
import javax.annotation.Generated;
import javax.persistence.metamodel.SetAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Bugs.class)
public abstract class Bugs_ extends fpt.dps.dtms.domain.AbstractAuditingEntity_ {

	public static volatile SingularAttribute<Bugs, String> code;
	public static volatile SetAttribute<Bugs, Attachments> attachments;
	public static volatile SetAttribute<Bugs, Notes> notes;
	public static volatile SingularAttribute<Bugs, String> stage;
	public static volatile SingularAttribute<Bugs, String> description;
	public static volatile SingularAttribute<Bugs, Integer> iteration;
	public static volatile SingularAttribute<Bugs, Long> id;
	public static volatile SingularAttribute<Bugs, String> physicalPath;
	public static volatile SingularAttribute<Bugs, BugResolution> resolution;
	public static volatile SingularAttribute<Bugs, Tasks> tasks;
	public static volatile SingularAttribute<Bugs, BugStatus> status;

}

