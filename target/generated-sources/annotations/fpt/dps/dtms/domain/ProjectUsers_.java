package fpt.dps.dtms.domain;

import fpt.dps.dtms.domain.enumeration.ProjectRoles;
import java.time.LocalDate;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(ProjectUsers.class)
public abstract class ProjectUsers_ extends fpt.dps.dtms.domain.AbstractAuditingEntity_ {

	public static volatile SingularAttribute<ProjectUsers, String> userLogin;
	public static volatile SingularAttribute<ProjectUsers, LocalDate> endDate;
	public static volatile SingularAttribute<ProjectUsers, ProjectRoles> roleName;
	public static volatile SingularAttribute<ProjectUsers, Projects> project;
	public static volatile SingularAttribute<ProjectUsers, Long> id;
	public static volatile SingularAttribute<ProjectUsers, LocalDate> startDate;
	public static volatile SingularAttribute<ProjectUsers, Float> effortPlan;

}

