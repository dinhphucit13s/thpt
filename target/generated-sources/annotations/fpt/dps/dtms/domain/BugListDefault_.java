package fpt.dps.dtms.domain;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(BugListDefault.class)
public abstract class BugListDefault_ extends fpt.dps.dtms.domain.AbstractAuditingEntity_ {

	public static volatile SingularAttribute<BugListDefault, BusinessLine> businessLine;
	public static volatile SingularAttribute<BugListDefault, String> description;
	public static volatile SingularAttribute<BugListDefault, Long> id;
	public static volatile SingularAttribute<BugListDefault, Boolean> status;

}

