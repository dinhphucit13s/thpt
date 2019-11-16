package fpt.dps.dtms.domain;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(MailReceiver.class)
public abstract class MailReceiver_ extends fpt.dps.dtms.domain.AbstractAuditingEntity_ {

	public static volatile SingularAttribute<MailReceiver, Mail> mail;
	public static volatile SingularAttribute<MailReceiver, String> from;
	public static volatile SingularAttribute<MailReceiver, Long> id;
	public static volatile SingularAttribute<MailReceiver, String> to;
	public static volatile SingularAttribute<MailReceiver, Boolean> status;

}

