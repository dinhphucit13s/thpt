package fpt.dps.dtms.domain;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(ProjectBugListDefault.class)
public abstract class ProjectBugListDefault_ extends fpt.dps.dtms.domain.AbstractAuditingEntity_ {

	public static volatile SingularAttribute<ProjectBugListDefault, String> code;
	public static volatile SingularAttribute<ProjectBugListDefault, BugListDefault> bugListDefault;
	public static volatile SingularAttribute<ProjectBugListDefault, Projects> project;
	public static volatile SingularAttribute<ProjectBugListDefault, Long> id;

}

