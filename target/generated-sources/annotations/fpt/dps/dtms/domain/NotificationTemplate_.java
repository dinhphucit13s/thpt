package fpt.dps.dtms.domain;

import fpt.dps.dtms.domain.enumeration.NotificationCategory;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(NotificationTemplate.class)
public abstract class NotificationTemplate_ extends fpt.dps.dtms.domain.AbstractAuditingEntity_ {

	public static volatile SingularAttribute<NotificationTemplate, String> template;
	public static volatile SingularAttribute<NotificationTemplate, String> description;
	public static volatile SingularAttribute<NotificationTemplate, Long> id;
	public static volatile SingularAttribute<NotificationTemplate, NotificationCategory> type;

}

