package fpt.dps.dtms.domain;

import fpt.dps.dtms.domain.enumeration.MONITORINGROLE;
import fpt.dps.dtms.domain.enumeration.PositionMonitoring;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(DtmsMonitoring.class)
public abstract class DtmsMonitoring_ extends fpt.dps.dtms.domain.AbstractAuditingEntity_ {

	public static volatile SingularAttribute<DtmsMonitoring, MONITORINGROLE> role;
	public static volatile SingularAttribute<DtmsMonitoring, Long> positionId;
	public static volatile SingularAttribute<DtmsMonitoring, String> members;
	public static volatile SingularAttribute<DtmsMonitoring, Long> id;
	public static volatile SingularAttribute<DtmsMonitoring, PositionMonitoring> position;

}

