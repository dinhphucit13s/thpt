package fpt.dps.dtms.domain;

import javax.annotation.Generated;
import javax.persistence.metamodel.SetAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(TmsThread.class)
public abstract class TmsThread_ extends fpt.dps.dtms.domain.AbstractAuditingEntity_ {

	public static volatile SingularAttribute<TmsThread, Projects> projects;
	public static volatile SingularAttribute<TmsThread, Integer> answers;
	public static volatile SingularAttribute<TmsThread, Boolean> closed;
	public static volatile SingularAttribute<TmsThread, Long> id;
	public static volatile SingularAttribute<TmsThread, ProjectUsers> assignee;
	public static volatile SingularAttribute<TmsThread, String> title;
	public static volatile SetAttribute<TmsThread, TmsPost> posts;
	public static volatile SingularAttribute<TmsThread, Integer> views;
	public static volatile SingularAttribute<TmsThread, Boolean> status;

}

