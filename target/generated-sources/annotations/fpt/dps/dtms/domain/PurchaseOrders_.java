package fpt.dps.dtms.domain;

import fpt.dps.dtms.domain.enumeration.PurchaseOrderStatus;
import java.time.Instant;
import javax.annotation.Generated;
import javax.persistence.metamodel.SetAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(PurchaseOrders.class)
public abstract class PurchaseOrders_ extends fpt.dps.dtms.domain.AbstractAuditingEntity_ {

	public static volatile SingularAttribute<PurchaseOrders, ProjectTemplates> projectTemplates;
	public static volatile SingularAttribute<PurchaseOrders, ProjectUsers> purchaseOrderLead;
	public static volatile SingularAttribute<PurchaseOrders, String> name;
	public static volatile SingularAttribute<PurchaseOrders, String> description;
	public static volatile SingularAttribute<PurchaseOrders, Projects> project;
	public static volatile SingularAttribute<PurchaseOrders, Instant> startTime;
	public static volatile SingularAttribute<PurchaseOrders, Long> id;
	public static volatile SingularAttribute<PurchaseOrders, Instant> endTime;
	public static volatile SingularAttribute<PurchaseOrders, String> reviewRatio;
	public static volatile SetAttribute<PurchaseOrders, Packages> packages;
	public static volatile SetAttribute<PurchaseOrders, TMSCustomFieldScreenValue> tmsCustomFieldScreenValues;
	public static volatile SingularAttribute<PurchaseOrders, PurchaseOrderStatus> status;

}

