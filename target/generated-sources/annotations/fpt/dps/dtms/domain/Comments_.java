package fpt.dps.dtms.domain;

import javax.annotation.Generated;
import javax.persistence.metamodel.SetAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Comments.class)
public abstract class Comments_ extends fpt.dps.dtms.domain.AbstractAuditingEntity_ {

	public static volatile SetAttribute<Comments, Attachments> attachments;
	public static volatile SingularAttribute<Comments, TmsPost> post;
	public static volatile SingularAttribute<Comments, Long> id;
	public static volatile SingularAttribute<Comments, String> content;

}

