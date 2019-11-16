package fpt.dps.dtms.domain;

import javax.annotation.Generated;
import javax.persistence.metamodel.SetAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(TmsPost.class)
public abstract class TmsPost_ extends fpt.dps.dtms.domain.AbstractAuditingEntity_ {

	public static volatile SingularAttribute<TmsPost, Integer> comments;
	public static volatile SetAttribute<TmsPost, Attachments> attachments;
	public static volatile SingularAttribute<TmsPost, Long> id;
	public static volatile SingularAttribute<TmsPost, TmsThread> thread;
	public static volatile SingularAttribute<TmsPost, String> content;

}

