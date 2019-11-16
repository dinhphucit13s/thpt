package fpt.dps.dtms.domain;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Notification.class)
public abstract class Notification_ extends fpt.dps.dtms.domain.AbstractAuditingEntity_ {

	public static volatile SingularAttribute<Notification, String> from;
	public static volatile SingularAttribute<Notification, Long> id;
	public static volatile SingularAttribute<Notification, String> to;
	public static volatile SingularAttribute<Notification, String> body;
	public static volatile SingularAttribute<Notification, Boolean> status;

}

