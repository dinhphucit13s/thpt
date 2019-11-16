package fpt.dps.dtms.domain;

import java.time.Instant;
import javax.annotation.Generated;
import javax.persistence.metamodel.SetAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Mail.class)
public abstract class Mail_ extends fpt.dps.dtms.domain.AbstractAuditingEntity_ {

	public static volatile SetAttribute<Mail, Attachments> attachments;
	public static volatile SingularAttribute<Mail, String> subject;
	public static volatile SingularAttribute<Mail, String> from;
	public static volatile SingularAttribute<Mail, Instant> startTime;
	public static volatile SingularAttribute<Mail, Long> id;
	public static volatile SingularAttribute<Mail, Instant> endTime;
	public static volatile SingularAttribute<Mail, String> body;

}

