package fpt.dps.dtms.domain;

import java.time.LocalDate;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(BusinessUnitManager.class)
public abstract class BusinessUnitManager_ extends fpt.dps.dtms.domain.AbstractAuditingEntity_ {

	public static volatile SingularAttribute<BusinessUnitManager, BusinessUnit> businessUnit;
	public static volatile SingularAttribute<BusinessUnitManager, User> manager;
	public static volatile SingularAttribute<BusinessUnitManager, String> description;
	public static volatile SingularAttribute<BusinessUnitManager, LocalDate> startTime;
	public static volatile SingularAttribute<BusinessUnitManager, Long> id;
	public static volatile SingularAttribute<BusinessUnitManager, LocalDate> endTime;

}

