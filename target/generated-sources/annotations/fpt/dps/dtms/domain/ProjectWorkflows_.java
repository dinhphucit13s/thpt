package fpt.dps.dtms.domain;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(ProjectWorkflows.class)
public abstract class ProjectWorkflows_ extends fpt.dps.dtms.domain.AbstractAuditingEntity_ {

	public static volatile SingularAttribute<ProjectWorkflows, String> opGridDTO;
	public static volatile SingularAttribute<ProjectWorkflows, ProjectTemplates> projectTemplates;
	public static volatile SingularAttribute<ProjectWorkflows, String> activity;
	public static volatile SingularAttribute<ProjectWorkflows, String> pmGridDTO;
	public static volatile SingularAttribute<ProjectWorkflows, String> entityDTO;
	public static volatile SingularAttribute<ProjectWorkflows, String> name;
	public static volatile SingularAttribute<ProjectWorkflows, String> description;
	public static volatile SingularAttribute<ProjectWorkflows, Integer> step;
	public static volatile SingularAttribute<ProjectWorkflows, Long> id;
	public static volatile SingularAttribute<ProjectWorkflows, String> inputDTO;
	public static volatile SingularAttribute<ProjectWorkflows, String> nextURI;

}

