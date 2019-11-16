package fpt.dps.dtms.domain;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(TMSCustomFieldScreenValue.class)
public abstract class TMSCustomFieldScreenValue_ extends fpt.dps.dtms.domain.AbstractAuditingEntity_ {

	public static volatile SingularAttribute<TMSCustomFieldScreenValue, TMSCustomFieldScreen> tmsCustomFieldScreen;
	public static volatile SingularAttribute<TMSCustomFieldScreenValue, Long> id;
	public static volatile SingularAttribute<TMSCustomFieldScreenValue, String> text;
	public static volatile SingularAttribute<TMSCustomFieldScreenValue, Packages> packages;
	public static volatile SingularAttribute<TMSCustomFieldScreenValue, String> value;
	public static volatile SingularAttribute<TMSCustomFieldScreenValue, PurchaseOrders> purchaseOrders;
	public static volatile SingularAttribute<TMSCustomFieldScreenValue, Tasks> tasks;

}

