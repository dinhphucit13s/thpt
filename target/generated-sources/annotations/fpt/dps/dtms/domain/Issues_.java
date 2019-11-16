package fpt.dps.dtms.domain;

import fpt.dps.dtms.domain.enumeration.IssueStatus;
import javax.annotation.Generated;
import javax.persistence.metamodel.SetAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Issues.class)
public abstract class Issues_ extends fpt.dps.dtms.domain.AbstractAuditingEntity_ {

	public static volatile SingularAttribute<Issues, Projects> projects;
	public static volatile SetAttribute<Issues, Attachments> attachments;
	public static volatile SingularAttribute<Issues, String> name;
	public static volatile SingularAttribute<Issues, PurchaseOrders> purchaseOrder;
	public static volatile SingularAttribute<Issues, String> description;
	public static volatile SingularAttribute<Issues, Long> id;
	public static volatile SingularAttribute<Issues, IssueStatus> status;

}

