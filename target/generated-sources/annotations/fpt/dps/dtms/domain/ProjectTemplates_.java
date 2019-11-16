package fpt.dps.dtms.domain;

import javax.annotation.Generated;
import javax.persistence.metamodel.SetAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(ProjectTemplates.class)
public abstract class ProjectTemplates_ extends fpt.dps.dtms.domain.AbstractAuditingEntity_ {

	public static volatile SingularAttribute<ProjectTemplates, byte[]> image;
	public static volatile SetAttribute<ProjectTemplates, Projects> projects;
	public static volatile SingularAttribute<ProjectTemplates, String> imageContentType;
	public static volatile SingularAttribute<ProjectTemplates, BusinessLine> businessLine;
	public static volatile SingularAttribute<ProjectTemplates, String> name;
	public static volatile SingularAttribute<ProjectTemplates, String> description;
	public static volatile SingularAttribute<ProjectTemplates, Long> id;

}

