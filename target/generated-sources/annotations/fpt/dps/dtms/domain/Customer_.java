package fpt.dps.dtms.domain;

import fpt.dps.dtms.domain.enumeration.CustomerType;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Customer.class)
public abstract class Customer_ extends fpt.dps.dtms.domain.AbstractAuditingEntity_ {

	public static volatile SingularAttribute<Customer, String> code;
	public static volatile SingularAttribute<Customer, String> name;
	public static volatile SingularAttribute<Customer, String> description;
	public static volatile SingularAttribute<Customer, Long> id;
	public static volatile SingularAttribute<Customer, CustomerType> type;
	public static volatile SingularAttribute<Customer, Boolean> status;

}

