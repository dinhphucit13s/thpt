package fpt.dps.dtms.domain;

import java.time.Instant;
import javax.annotation.Generated;
import javax.persistence.metamodel.SetAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Packages.class)
public abstract class Packages_ extends fpt.dps.dtms.domain.AbstractAuditingEntity_ {

	public static volatile SingularAttribute<Packages, String> op;
	public static volatile SingularAttribute<Packages, String> delivery;
	public static volatile SingularAttribute<Packages, String> fi;
	public static volatile SingularAttribute<Packages, Instant> estimateDelivery;
	public static volatile SingularAttribute<Packages, String> description;
	public static volatile SingularAttribute<Packages, String> reviewer;
	public static volatile SingularAttribute<Packages, Integer> target;
	public static volatile SingularAttribute<Packages, String> name;
	public static volatile SingularAttribute<Packages, Instant> startTime;
	public static volatile SingularAttribute<Packages, Long> id;
	public static volatile SingularAttribute<Packages, Instant> endTime;
	public static volatile SingularAttribute<Packages, PurchaseOrders> purchaseOrders;
	public static volatile SetAttribute<Packages, TMSCustomFieldScreenValue> tmsCustomFieldScreenValues;
	public static volatile SetAttribute<Packages, Tasks> tasks;

}

