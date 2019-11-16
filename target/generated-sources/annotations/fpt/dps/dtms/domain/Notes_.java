package fpt.dps.dtms.domain;

import javax.annotation.Generated;
import javax.persistence.metamodel.SetAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Notes.class)
public abstract class Notes_ extends fpt.dps.dtms.domain.AbstractAuditingEntity_ {

	public static volatile SetAttribute<Notes, Attachments> attachments;
	public static volatile SingularAttribute<Notes, Bugs> bug;
	public static volatile SingularAttribute<Notes, String> description;
	public static volatile SingularAttribute<Notes, Long> id;
	public static volatile SingularAttribute<Notes, Tasks> tasks;

}

