package fpt.dps.dtms.domain;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Attachments.class)
public abstract class Attachments_ extends fpt.dps.dtms.domain.AbstractAuditingEntity_ {

	public static volatile SingularAttribute<Attachments, Bugs> bugs;
	public static volatile SingularAttribute<Attachments, String> filename;
	public static volatile SingularAttribute<Attachments, Notes> notes;
	public static volatile SingularAttribute<Attachments, Mail> mail;
	public static volatile SingularAttribute<Attachments, TmsPost> post;
	public static volatile SingularAttribute<Attachments, Comments> comment;
	public static volatile SingularAttribute<Attachments, Long> id;
	public static volatile SingularAttribute<Attachments, Issues> issues;
	public static volatile SingularAttribute<Attachments, String> fileType;
	public static volatile SingularAttribute<Attachments, String> diskFile;

}

