package fpt.dps.dtms.domain;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(TMSLogHistory.class)
public abstract class TMSLogHistory_ extends fpt.dps.dtms.domain.AbstractAuditingEntity_ {

	public static volatile SingularAttribute<TMSLogHistory, String> newValue;
	public static volatile SingularAttribute<TMSLogHistory, Projects> projects;
	public static volatile SingularAttribute<TMSLogHistory, String> action;
	public static volatile SingularAttribute<TMSLogHistory, Long> id;
	public static volatile SingularAttribute<TMSLogHistory, String> oldValue;
	public static volatile SingularAttribute<TMSLogHistory, Packages> packages;
	public static volatile SingularAttribute<TMSLogHistory, PurchaseOrders> purchaseOrders;
	public static volatile SingularAttribute<TMSLogHistory, Tasks> tasks;

}

