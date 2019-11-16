package fpt.dps.dtms.domain;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(TMSCustomFieldScreen.class)
public abstract class TMSCustomFieldScreen_ extends fpt.dps.dtms.domain.AbstractAuditingEntity_ {

	public static volatile SingularAttribute<TMSCustomFieldScreen, Integer> sequence;
	public static volatile SingularAttribute<TMSCustomFieldScreen, String> entityGridOp;
	public static volatile SingularAttribute<TMSCustomFieldScreen, TMSCustomField> tmsCustomField;
	public static volatile SingularAttribute<TMSCustomFieldScreen, ProjectWorkflows> projectWorkflows;
	public static volatile SingularAttribute<TMSCustomFieldScreen, Long> id;
	public static volatile SingularAttribute<TMSCustomFieldScreen, String> entityGridPm;
	public static volatile SingularAttribute<TMSCustomFieldScreen, String> entityGridInput;

}

